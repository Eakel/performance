package com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bsh.Interpreter;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.session.SessionManager;
import com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.interfaces.ITask;
import com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.javacommand.IJavaCommand;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonLRecord;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPExec;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPHost;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPInfo;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPTable;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonWTrigger;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MiscUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MonTableDataSourceUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MutilRtn;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.SSHUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.TimeUtil;

/**
 * 单个List&lt;MonPInfo>执行Task
 * @author linzhaoming
 * 
 * Created at 2012-9-17
 */
public class MonTaskImpl implements ITask {
	private static transient Log log = LogFactory.getLog(MonTaskImpl.class);

	/**
	 * 执行任务，并将结果保存到表[MON_L_RECORD]中，按年月分表
	 */
	public void doTask(List list) throws Exception {
		MonPInfo objMonPInfo = (MonPInfo) list.get(0);
		try {
			SessionManager.getSession().startTransaction();
			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);

			String host = null;
			String ip = null;
			Timestamp start = new Timestamp(System.currentTimeMillis());
			String rtn = null;
			if (objMonPInfo.getType().equalsIgnoreCase("EXEC")) {	//EXEC类型
				MonPExec objMonPExec = objIMonSV.getMonPExecByExecId(objMonPInfo.getTypeId());
				if (objMonPExec.getType().equalsIgnoreCase("SHELL")) {
					MonPHost objMonPHost = objIMonSV.getMonPHostByHostname(objMonPInfo.getHostname());
					host = objMonPHost.getHostname();
					ip = objMonPHost.getIp();

					rtn = SSHUtil.ssh4Shell(objMonPHost.getIp(), (int) objMonPHost.getSshport(), objMonPHost.getUsername(), objMonPHost.getPassword(), 
							String.valueOf(objMonPInfo.getInfoId()), objMonPExec.getExpr());
				} else if (objMonPExec.getType().equalsIgnoreCase("COMMAND")) {
					MonPHost objMonPHost = objIMonSV.getMonPHostByHostname(objMonPInfo.getHostname());
					host = objMonPHost.getHostname();
					ip = objMonPHost.getIp();
					rtn = SSHUtil.ssh4Command(objMonPHost.getIp(), (int) objMonPHost.getSshport(), objMonPHost.getUsername(), objMonPHost.getPassword(), objMonPExec.getExpr());
				} else if (objMonPExec.getType().equalsIgnoreCase("JAVACOMMAND")) {	//第一个参数为Java类，第二个参数为参数
					//配置例子：com.asiainfo.mon.exe.task.javacommand.OracleAWRCommand;so1,YYDB1-YYDB2,/app/bjmon/awr/
					String[] tmp = StringUtils.split(objMonPExec.getExpr(), ";");
					IJavaCommand obj = (IJavaCommand) Class.forName(tmp[0].trim()).newInstance();
					rtn = obj.execute(tmp[1].trim());
				} else {
					throw new Exception("无法识别的执行类型:" + objMonPExec.getType());
				}
			} else if (objMonPInfo.getType().equalsIgnoreCase("TABLE")) {	//TABLE类型
				MonPTable objMonPTable = objIMonSV.getMonPTableByTableId(objMonPInfo.getTypeId());
				List l = new ArrayList();
				Connection conn = null;
				PreparedStatement ptmt = null;
				ResultSet rs = null;
				try {
					conn = MonTableDataSourceUtil.getConnection(objMonPTable.getDbUrlname(), objMonPTable.getDbAcctCode());
					String sql = null;
					if (StringUtils.contains(objMonPTable.getSql(), "$")) {
						Calendar objCalendar = Calendar.getInstance();
						objCalendar.setTime(new Date());
						int year = objCalendar.get(1);
						int shortyear = Integer.parseInt(TimeUtil.format8(objCalendar.getTime()));
						String month = TimeUtil.getMM(objCalendar.getTime());
						String day = TimeUtil.getDD(objCalendar.getTime());
						sql = objMonPTable.getSql();
						sql = StringUtils.replace(sql, "$SHORTYEAR", String.valueOf(shortyear));
						sql = StringUtils.replace(sql, "$YEAR", String.valueOf(year));
						sql = StringUtils.replace(sql, "$MONTH", String.valueOf(month));
						sql = StringUtils.replace(sql, "$DAY", String.valueOf(day));
						sql = StringUtils.replace(sql, "$HH_1", TimeUtil.getHH(TimeUtil.addOrMinusHours(objCalendar.getTimeInMillis(), -1)));
						sql = StringUtils.replace(sql, "$HH", TimeUtil.getHH(objCalendar.getTime()));
					} else {
						sql = objMonPTable.getSql();
					}

					ptmt = conn.prepareStatement(sql);
					rs = ptmt.executeQuery();
					while (rs.next()) {
						l.add(rs.getString(1));
					}
				} catch (Throwable ex) {
					log.error("查询出错", ex);
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
				
				rtn = StringUtils.join(l.iterator(), "|");
				log.debug(rtn);
			}

			//处理返回值，例子: MEM_USED_PERCENT:91|CPU_IDLE_PERCENT:100|DISK_USED_PERCENT_ROOT:17
			//将结果保存到表[MON_L_RECORD]中，按年月分表
			if (!StringUtils.isBlank(rtn)) {
				String[] rtnValue = StringUtils.split(rtn, "|");

				MutilRtn[] objMutilRtn = null;
				try {
					Interpreter interpreter = new Interpreter();
					interpreter.setClassLoader(Thread.currentThread().getContextClassLoader());
					if (!StringUtils.isBlank(objMonPInfo.getHostname())) {
						if (!StringUtils.isBlank(host)) {
							interpreter.set("$HOST", host);
						}
						if (!StringUtils.isBlank(ip)) {
							interpreter.set("$IP", ip);
						}
					}
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time = dateFormat.format(new Date());
					interpreter.set("$TIME", time);
					interpreter.set("$INFO_NAME", objMonPInfo.getName());
					if (((rtnValue != null ? 1 : 0) & (rtnValue.length != 0 ? 1 : 0)) != 0) {
						HashMap map = new HashMap();
						for (int i = 0; i < rtnValue.length; i++) {
							String[] tmp = StringUtils.split(rtnValue[i], ":");	//例如MEM_USED_PERCENT:91
							if (map.containsKey(tmp[0])) {
								List l = (List) map.get(tmp[0]);
								l.add(tmp[1]);
								map.put(tmp[0], l);
							} else {
								List l = new ArrayList();
								l.add(tmp[1]);
								map.put(tmp[0], l);
							}
						}

						Set key = map.keySet();
						for (Iterator iter = key.iterator(); iter.hasNext();) {
							String item = (String) iter.next();
							String[] tmp = (String[]) ((List) map.get(item)).toArray(new String[0]);
							if (tmp != null) {
								for (int i = 0; i < tmp.length; i++) {
									if (tmp[i] != null) {
										tmp[i] = tmp[i].trim();
									}
								}
							}
							interpreter.set("$" + item, tmp);
						}
					}

					String thresholdExpr = objIMonSV.getMonPThresholdByThresholId(objMonPInfo.getThresholdId()).getAllExpr();
					interpreter.eval(thresholdExpr);
					Object object = interpreter.get("RTN");
					if ((object instanceof MutilRtn)) {
						objMutilRtn = new MutilRtn[] { (MutilRtn) object };
					} else if ((object instanceof MutilRtn[])) {
						objMutilRtn = (MutilRtn[]) object;
					}
				} catch (Throwable ex) {
					log.error("执行阀值表达式错误", ex);
					throw ex;
				}

				if ((objMutilRtn != null) && (objMutilRtn.length != 0)) {
					for (int i = 0; i < objMutilRtn.length; i++) {
						MonLRecord objMonLRecord = new MonLRecord();
						if(MiscUtil.getDBDialect().equalsIgnoreCase("Oracle")){
							objMonLRecord.setRecordId(objIMonSV.getNextId("MON_L_RECORD$SEQ"));
						}
						//MySQL使用自动增长列
						
						objMonLRecord.setInfoId(objMonPInfo.getInfoId());
						objMonLRecord.setBusiArea(objMonPInfo.getBusiArea());
						objMonLRecord.setMonType(objMonPInfo.getType());
						objMonLRecord.setHostname(objMonPInfo.getHostname());
						objMonLRecord.setInfoName(objMonPInfo.getName());
						objMonLRecord.setInfoValue(rtnValue[i]);
						objMonLRecord.setCreateDate(start);
						objMonLRecord.setDoneDate(new Timestamp(System.currentTimeMillis()));

						if (!StringUtils.isBlank(ip)) {
							objMonLRecord.setIp(ip);
						}

						if (objMutilRtn[i].level > 0) {
							objMonLRecord.setIsTriggerWarn("Y");
						} else {
							objMonLRecord.setIsTriggerWarn("N");
						}
						objMonLRecord.setWarnLevel(String.valueOf(objMutilRtn[i].level));
						objIMonSV.insertMonLRecord(objMonLRecord);

						if (objMutilRtn[i].level <= 0) {
							continue;
						}
						String[] phone = objIMonSV.getPhonenumByInfoId(objMonPInfo.getInfoId());
						MonWTrigger objMonWTrigger = new MonWTrigger();
						objMonWTrigger.setInfoId(objMonPInfo.getInfoId());
						objMonWTrigger.setInfoName(objMonPInfo.getName());
						objMonWTrigger.setPhonenum(StringUtils.join(phone, "|"));
						objMonWTrigger.setContent(objMutilRtn[i].msg);
						objMonWTrigger.setWarnLevel(String.valueOf(objMutilRtn[i].level));
						objMonWTrigger.setCreateDate(new Timestamp(System.currentTimeMillis()));
						objMonWTrigger.setState("C");

						if (!StringUtils.isBlank(ip)) {
							objMonWTrigger.setIp(ip);
						}
						objMonWTrigger.setRecordId(objMonLRecord.getRecordId());
						objIMonSV.insertMonWTrigger(objMonWTrigger);
					}
				}
			}

			SessionManager.getSession().commitTransaction();
		} catch (Throwable ex) {
			log.error("info_id:" + objMonPInfo.getInfoId() + ",执行失败", ex);
			SessionManager.getSession().rollbackTransaction();
			throw new Exception(ex);
		}
	}

	public static void main(String[] args) throws Exception {
	}
}