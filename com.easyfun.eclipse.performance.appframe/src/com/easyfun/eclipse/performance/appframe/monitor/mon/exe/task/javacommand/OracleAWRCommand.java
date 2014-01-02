package com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.javacommand;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MiscUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MonTableDataSourceUtil;

/**
 * �ռ�AWR��־
 * <li>�������ӣ�com.asiainfo.mon.exe.task.javacommand.OracleAWRCommand;so1,YYDB1-YYDB2,/app/bjmon/awr/
 * @author linzhaoming
 * 
 * Created at 2012-9-22
 */
public class OracleAWRCommand implements IJavaCommand {
	private static transient Log log = LogFactory.getLog(OracleAWRCommand.class);
	private static Map LASTSNAP = new ConcurrentHashMap();

	/** ��ȡָ�����ݿ��AWR����Ϣ List&lt;SnapShot>
	 * ��ȡ�������SnapShot
	 * <li>urlΪ��ӦMON_DB_URL.NAME
	 * <li>dbAcctCode��ӦMON_DB_ACCT.DB_ACCT_CODE
	 * */
	private List getSnapShot(String url, String dbAcctCode) throws Exception {
		//so1,YYDB1-YYDB2,/app/bjmon/awr/
		//urlΪYYDB2����ӦMON_DB_URL.NAME 
		//dbAcctCodeΪso1����ӦMON_DB_ACCT.DB_ACCT_CODE
		List snapshot = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = MonTableDataSourceUtil.getConnection(url, dbAcctCode);
			ptmt = conn.prepareStatement("select * from (select a.snap_id, to_char(a.BEGIN_INTERVAL_TIME,'yyyymmddhh24mi') as begin_date,to_char(a.end_interval_time,'yyyymmddhh24mi') as end_date, a.dbid, a.instance_number,c.INSTANCE_NAME,UTL_INADDR.get_host_name  from dba_hist_snapshot a,v$database b,v$instance c where a.dbid = b.DBID  and a.instance_number = c.INSTANCE_NUMBER order by a.begin_interval_time desc ) where rownum < 3");
			rs = ptmt.executeQuery();
			while (rs.next()) {
				SnapShot objSnapShot = new SnapShot();
				objSnapShot.snapId = rs.getLong(1);
				objSnapShot.beginDate = rs.getString(2);
				objSnapShot.endDate = rs.getString(3);
				objSnapShot.dbId = rs.getLong(4);
				objSnapShot.instanceNumber = rs.getLong(5);
				objSnapShot.instanceName = rs.getString(6);
				objSnapShot.hostname = rs.getString(7);
				snapshot.add(objSnapShot);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return snapshot;
	}

	/** ��Oracle�л�ȡAWR��HTML��Ϣ ���ɵ�AWR����־Ŀ¼�� .html.gz*/
	private void createAwrHtml(String url, String dbAcctCode, long dbId, long instanceNumber, long startSnapId, long endSnapId, String instanceName, String hostName, String beginDate, String endDate) throws Exception {
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = MonTableDataSourceUtil.getConnection(url, dbAcctCode);
			//�����б�[dbId] [instanceNumber] [beginDate] [endDate]
			ptmt = conn.prepareStatement("select * from table(DBMS_WORKLOAD_REPOSITORY.AWR_REPORT_HTML(?,?,?,?))");
			ptmt.setFetchSize(1000);
			ptmt.setLong(1, dbId);
			ptmt.setLong(2, instanceNumber);
			ptmt.setLong(3, startSnapId);
			ptmt.setLong(4, endSnapId);
			rs = ptmt.executeQuery();

			StringBuilder sb = new StringBuilder();
			while (rs.next()) {
				sb.append(rs.getString(1));
			}

			String path = MiscUtil.getAwrPath() + "/" + instanceName + "@" + hostName + "/" + StringUtils.substring(beginDate, 0, 8);
			if (!new File(path).isDirectory()) {
				FileUtils.forceMkdir(new File(path));
			}

			String fileName = path + "/" + instanceName + "@" + hostName + "_" + beginDate + "_" + endDate + ".html.gz";

			GZIPOutputStream out = null;
			try {
				out = new GZIPOutputStream(new FileOutputStream(fileName));
				out.write(sb.toString().getBytes());
			} finally {
				if (out != null) {
					out.finish();
					out.close();
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null)
				conn.close();
		}
	}

	public String execute(String in) throws Exception {
		String[] tmp = StringUtils.split(in, ",");	//url, dbAcctCode ���ӣ�so1,YYDB1-YYDB2,/app/bjmon/awr/
		if (tmp.length < 2) {
			throw new Exception("����:" + in + ",�ö��ŷָ�����鳤��С��2");
		}

		List snapshot = getSnapShot(tmp[1], tmp[0]);

		if (snapshot.size() > 1) {
			SnapShot end = (SnapShot) snapshot.get(0);
			SnapShot start = (SnapShot) snapshot.get(1);

			String key = end.instanceName;
			Object obj = LASTSNAP.get(key);
			if ((obj == null) || (((Long) obj).longValue() != end.snapId)) {
				createAwrHtml(tmp[1], tmp[0], end.dbId, end.instanceNumber, start.snapId, end.snapId, end.instanceName, end.hostname, end.beginDate, end.endDate);
				LASTSNAP.put(key, new Long(end.snapId));
			}
		} else {
			log.error("��õĿ�������С��2,������awr����");
		}

		return "";
	}

	public static void main(String[] args) throws Exception {
		String a = "so1,ZJCSD1-ZJCSD2";
		OracleAWRCommand y = new OracleAWRCommand();
		y.execute(a);
	}

	/** �����ѯOracle��AWR����Ϣ*/
	public class SnapShot {
		long snapId;
		String beginDate;
		String endDate;
		long dbId;
		long instanceNumber;
		String instanceName;
		String hostname;

		public SnapShot() {
		}
	}
}