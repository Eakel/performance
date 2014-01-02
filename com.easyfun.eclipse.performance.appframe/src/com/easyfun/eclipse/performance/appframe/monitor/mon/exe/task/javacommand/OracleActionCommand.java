package com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.javacommand;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MonTableDataSourceUtil;

/**
 * ֧�����ֶ���
 * <li>"SHRINK"��������
 * <li>"GT"����ȡ��״̬
 * @author linzhaoming
 * 
 * Created at 2012-9-22
 */
public class OracleActionCommand implements IJavaCommand {
	private static transient Log log = LogFactory.getLog(OracleActionCommand.class);

	/** [actions] [dbAcctCode] [dbAcctUrl] [tableNames]
	 * <li>[actions]��"+"��Ϊ�ָ����:"SHRINK"+"GT"��ֻ����"GT"��
	 * <li>[tableNames]��:��Ϊ�ָ� {owner}.{table}
	 * */
	public String execute(String in) throws Exception {
		String[] tmp = StringUtils.split(in, ",");
		if (tmp.length < 4) {
			throw new Exception("����:" + in + ",�ö��ŷָ�����鳤��С��4");
		}

		String[] actions = StringUtils.split(tmp[0].trim(), "+");
		String dbAcctCode = tmp[1].trim();
		String dbAcctUrl = tmp[2].trim();
		String[] tableNames = tmp[3].trim().split(":");

		for (int i = 0; i < tableNames.length; i++) {
			String owner = null;
			String table = null;
			if (tableNames[i].indexOf(".") != -1) {
				String[] tmp2 = StringUtils.split(tableNames[i], ".");
				if ((tmp2 == null) || (tmp2.length != 2)) {
					throw new Exception("�޷��ָ�");
				}
				owner = tmp2[0].trim().toUpperCase();
				table = tmp2[1].trim().toUpperCase();
			} else {
				owner = dbAcctCode;
				table = tableNames[i];
			}

			//�滻��������ģ��Ϊʱ�����ʽ
			if (table.indexOf("${YYYYMMDD}") != -1) {		//"${YYYYMMDD}"�滻Ϊ��ǰ���ڣ���ʽΪ"yyyyMMdd"
				SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
				String split = DATE_FORMAT.format(new Date());
				table = StringUtils.replace(table, "${YYYYMMDD}", split);
			} else if (table.indexOf("${YYYYMM}") != -1) {	//"${YYYYMM}"�滻Ϊ��ǰ���ڣ���ʽΪ"yyyyMMdd"
				SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMM");
				String split = DATE_FORMAT.format(new Date());
				table = StringUtils.replace(table, "${YYYYMM}", split);
			} else if (table.indexOf("${YYYY}") != -1) {	//"${YYYY}"�滻Ϊ��ǰ���ڣ���ʽΪ"yyyy"
				SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy");
				String split = DATE_FORMAT.format(new Date());
				table = StringUtils.replace(table, "${YYYY}", split);
			}

			for (int j = 0; j < actions.length; j++) {
				if (actions[j].equalsIgnoreCase("SHRINK")) {
					try {
						doShrinkAction(dbAcctUrl, dbAcctCode, owner, table);
					} catch (Throwable ex) {
						log.error("dbAcctUrl=" + dbAcctUrl + ",dbAcctCode=" + dbAcctCode + ",owner=" + owner + ",table=" + table + ",����ʧ��", ex);
					}
				} else if (actions[j].equalsIgnoreCase("GT")) {
					try {
						doGTAction(dbAcctUrl, dbAcctCode, owner, table);
					} catch (Throwable ex) {
						log.error("dbAcctUrl=" + dbAcctUrl + ",dbAcctCode=" + dbAcctCode + ",owner=" + owner + ",table=" + table + ",����ʧ��", ex);
					}
				} else {
					throw new Exception("û��ʵ�ֵ�action:" + actions[j]);
				}
			}
		}

		return "�������";
	}

	/** ������
	 * <li>alter table [TABLE_NAME] enable row movement
	 * <li>alter table [TABLE_NAME] shrink space
	 * <li>alter table [TABLE_NAME] disable row movement
	 * */
	private void doShrinkAction(String dbAcctUrl, String dbAcctCode, String owner, String tableName) throws Exception {
		Connection conn = null;
		PreparedStatement ptmt1 = null;
		PreparedStatement ptmt2 = null;
		PreparedStatement ptmt3 = null;
		try {
			conn = MonTableDataSourceUtil.getConnection(dbAcctUrl, dbAcctCode);
			ptmt1 = conn.prepareStatement("alter table " + owner.toUpperCase() + "." + tableName.toUpperCase() + " enable row movement");
			ptmt1.execute();

			ptmt2 = conn.prepareStatement("alter table " + owner.toUpperCase() + "." + tableName.toUpperCase() + " shrink space");
			ptmt2.execute();

			ptmt3 = conn.prepareStatement("alter table " + owner.toUpperCase() + "." + tableName.toUpperCase() + " disable row movement");
			ptmt3.execute();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (ptmt1 != null) {
				ptmt1.close();
			}
			if (ptmt2 != null) {
				ptmt2.close();
			}
			if (ptmt3 != null) {
				ptmt3.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	/** ִ�� "call dbms_stats.gather_table_stats"*/
	private void doGTAction(String dbAcctUrl, String dbAcctCode, String owner, String tableName) throws Exception {
		Connection conn = null;
		CallableStatement ctmt = null;
		try {
			conn = MonTableDataSourceUtil.getConnection(dbAcctUrl, dbAcctCode);
			ctmt = conn.prepareCall("call dbms_stats.gather_table_stats(?,?,null,?)");
			ctmt.setString(1, owner.toUpperCase());
			ctmt.setString(2, tableName.toUpperCase());
			ctmt.setInt(3, 25);
			ctmt.execute();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (ctmt != null) {
				ctmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		OracleActionCommand a = new OracleActionCommand();
		a.execute("SHRINK+GT,test1,test1,test1.t2:test1.t3");
	}
}