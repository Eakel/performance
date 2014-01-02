package com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonLRecord;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPExec;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPHost;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPInfo;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonWTrigger;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MiscUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MutilRtn;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.SSHUtil;

/**
 * 多个List&lt;MonPInfo>执行
 * <li>不支持TABLE的形式
 * 
 * @author linzhaoming
 * 
 * Created at 2013-1-4
 */
public class MonMergeTaskImpl implements ITask {
	private static transient Log log = LogFactory.getLog(MonMergeTaskImpl.class);

	public void doTask(List in) throws Exception {
		MonPInfo[] objMergeMonPInfo = (MonPInfo[]) in.toArray(new MonPInfo[0]);
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);

		String host = null;
		String ip = null;
		Timestamp start = new Timestamp(System.currentTimeMillis());

		MonPInfo objInfo = objMergeMonPInfo[0];
		List list1 = new ArrayList();
		List list2 = new ArrayList();

		if (objInfo.getType().equalsIgnoreCase("EXEC")) {
			for (int i = 0; i < objMergeMonPInfo.length; i++) {
				MonPExec objMonPExec = objIMonSV.getMonPExecByExecId(objMergeMonPInfo[i].getTypeId());
				if (objMonPExec.getType().equalsIgnoreCase("SHELL")) {
					list1.add(String.valueOf(objMergeMonPInfo[i].getInfoId()));
					list2.add(objMonPExec.getExpr());
				} else {
					if (objMonPExec.getType().equalsIgnoreCase("COMMAND")) {
						throw new Exception("合并任务中无法识别的执行类型:" + objMonPExec.getType());
					}
					if (objMonPExec.getType().equalsIgnoreCase("JAVACOMMAND")) {
						throw new Exception("合并任务中无法识别的执行类型:" + objMonPExec.getType());
					}
					throw new Exception("合并任务中无法识别的执行类型:" + objMonPExec.getType());
				}
			}
		} else if (objInfo.getType().equalsIgnoreCase("TABLE")) {
			throw new Exception("执行方式为table的在合并task中没有实现");
		}

		MonPHost objMonPHost = objIMonSV.getMonPHostByHostname(objMergeMonPInfo[0].getHostname());
		String[] returnStr = SSHUtil.ssh4Shell(objMonPHost.getIp(), (int) objMonPHost.getSshport(), objMonPHost.getUsername(),
				objMonPHost.getPassword(), (String[]) list1.toArray(new String[0]), (String[]) list2.toArray(new String[0]));

		for (int k = 0; k < returnStr.length; k++) {
			String rtn = returnStr[k];
			MonPInfo objMonPInfo = objMergeMonPInfo[k];
			try {
				SessionManager.getSession().startTransaction();
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
						HashMap map;
						Iterator iter;
						if (((rtnValue != null ? 1 : 0) & (rtnValue.length != 0 ? 1 : 0)) != 0) {
							map = new HashMap();
							for (int i = 0; i < rtnValue.length; i++) {
								String[] tmp = StringUtils.split(rtnValue[i], ":");
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
							for (iter = key.iterator(); iter.hasNext();) {
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
						interpreter.eval(thresholdExpr);	// info_id:1000085,执行失败,返回数据PROC_CNT:10
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
				log.error("info_id:" + objMonPInfo.getInfoId() + ",执行失败,返回数据" + rtn, ex);
				SessionManager.getSession().rollbackTransaction();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("mon.is_merge_exec", "Y");

		HashMap map = new HashMap();

		map.put("100008", "100008");
		map.put("100013", "100013");

		List list = new ArrayList();
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		MonPInfo[] obj = objIMonSV.getAllMonPInfo();

		for (int i = 0; i < obj.length; i++) {
			if (map.containsKey(String.valueOf(obj[i].getInfoId()))) {
				list.add(obj[i]);
			}

		}

		MonMergeTaskImpl a = new MonMergeTaskImpl();
		a.doTask(list);
	}
}