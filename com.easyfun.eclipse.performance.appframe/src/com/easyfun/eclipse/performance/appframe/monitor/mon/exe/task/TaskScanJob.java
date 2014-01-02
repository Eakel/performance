package com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPInfo;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;

/**
 * Task��JOBʵ����
 * @author linzhaoming
 * 
 * Created at 2012-9-17
 */
public class TaskScanJob implements Job {
	private static transient Log log = LogFactory.getLog(TaskScanJob.class);

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			_execute(context);
		} catch (Throwable ex) {
			log.error("ִ��ɨ����������쳣", ex);
		}
	}

	private void _execute(JobExecutionContext context) throws Exception {
		log.info("ɨ������ִ��");
		IMonSV objITaskSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		MonPInfo[] objMonPInfo = objITaskSV.getAllMonPInfo();

		if ((objMonPInfo == null) || (objMonPInfo.length == 0)) {
			log.info("ɨ�赽����Ҫ�������Ϊ��,��β�����");
			return;
		}

		List runningJobs = context.getScheduler().getCurrentlyExecutingJobs();
		if (log.isDebugEnabled()) {
			for (Iterator iter = runningJobs.iterator(); iter.hasNext();) {
				JobExecutionContext item = (JobExecutionContext) iter.next();
				if ((item.getJobInstance() instanceof TaskJob)) {
					Object obj = item.getJobDetail().getJobDataMap().get(TaskFrameWork.MON_P_INFO);
					if(obj instanceof MonPInfo){
						log.debug("ϵͳ�л���δ��ɵ�����,����ID:" + ((MonPInfo) obj).getInfoId() + ",ִ����:" + item.getJobDetail().getJobClass().getName());
					}else if(obj instanceof List){
						log.debug("ϵͳ�л���δ��ɵ�����(List),��СΪ:" + ((List)obj).size());
						for (Object t : (List)obj) {
							if(t instanceof MonPInfo){
								log.debug("ϵͳ�л���δ��ɵ�����,����ID:" + ((MonPInfo) t).getInfoId() + ",ִ����:" + item.getJobDetail().getJobClass().getName());
							}
						}
					}
				}
			}
		}

		String[] jobNames = context.getScheduler().getJobNames(TaskFrameWork.TASK_JOB_GROUP);
		if ((jobNames != null) && (jobNames.length != 0))
			for (int i = 0; i < jobNames.length; i++) {
				if (isRunning(jobNames[i], TaskFrameWork.TASK_JOB_GROUP, runningJobs)) {
					log.info("job����:" + jobNames[i] + ",�����������У��˴β�����ش�������.�ȴ����������н���,�����´�ɨ���ʱ��,�Ż���ش�����");
				} else {
					try {
						removeJobAndTrigger(jobNames[i], TaskFrameWork.TASK_JOB_GROUP, context);
					} catch (Exception ex2) {
						log.info("job����:" + jobNames[i] + ",�ӵ�������ɾ��ʧ��,���ڲ�ɾ��,�´�ɾ��", ex2);
					}
				}
			}
		
		HashMap map;	//keyΪ�������ƣ�valueΪList<MonPInfo>
		if (TaskFrameWork.isMergeExecTask()) {
			//�ϲ����񣬼�ص�ʵ������
			map = new HashMap();

			for (int i = 0; i < objMonPInfo.length; i++) {
				long infoId = objMonPInfo[i].getInfoId();
				try {
					String key = null;
					//��ΪEXEC���� ��[HOSTNAME]^[TIME_ID]^[TYPE]��ΪKEY��������[INFO_ID]��ΪKEY
					if ((!StringUtils.isBlank(objMonPInfo[i].getHostname())) && (objMonPInfo[i].getTimeId() > 0L) && (objMonPInfo[i].getType().equals("EXEC"))) {
						key = objMonPInfo[i].getHostname() + "^" + objMonPInfo[i].getTimeId() + "^" + objMonPInfo[i].getType();
					} else {
						key = String.valueOf(infoId);
					}

					if (isRunning("job_" + key, TaskFrameWork.TASK_JOB_GROUP, runningJobs)) {
						log.info("job����:" + key + ",�����������У��˴β�����ش�������.�ȴ����������н���,�����´�ɨ���ʱ��,�Ż���ش�����");
					} else if (map.containsKey(key)) {
						List list = (List) map.get(key);
						list.add(objMonPInfo[i]);
					} else {
						List list = new ArrayList();
						list.add(objMonPInfo[i]);
						map.put(key, list);
					}
				} catch (Exception ex) {
					log.error("����ID:" + infoId + ",�޷�����", ex);
				}
			}

			Set key = map.keySet();
			for (Iterator iter = key.iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				List list = (List) map.get(item);
				if (list.size() == 1) {	//����ֻ��һ��
					MonPInfo obj = (MonPInfo) list.get(0);
					String jobName = "job_" + obj.getInfoId();
					String jobGroupName = TaskFrameWork.TASK_JOB_GROUP;
					String triggerName = "trigger_" + obj.getInfoId();
					String triggerGroupName = TaskFrameWork.TASK_TRIGGER_GROUP;
					JobDetail job = new JobDetail(jobName, jobGroupName, TaskJob.class);
					Trigger trigger = null;
					String cron = obj.getExpr();
					if (StringUtils.isBlank(cron)) {
						log.error("����ID:" + obj.getInfoId() + ",���õ�����ִ��ʱ��Ϊ��");
						continue;
					}
					trigger = new CronTrigger(triggerName, triggerGroupName, cron);
					job.getJobDataMap().put(TaskFrameWork.MON_P_INFO, list);
					context.getScheduler().scheduleJob(job, trigger);
					if (log.isDebugEnabled()) {
						log.debug("������ȵ�����ID:" + obj.getInfoId());
					}
				} else {	//�����ж��
					MonPInfo obj = (MonPInfo) list.get(0);
					String jobName = "job_" + item;
					String jobGroupName = TaskFrameWork.TASK_JOB_GROUP;
					String triggerName = "trigger_" + item;
					String triggerGroupName = TaskFrameWork.TASK_TRIGGER_GROUP;
					JobDetail job = new JobDetail(jobName, jobGroupName, TaskJob.class);

					Trigger trigger = null;
					String cron = obj.getExpr();
					if (StringUtils.isBlank(cron)) {
						log.error("�ϲ�����KEY:" + obj.getInfoId() + ",���õ�����ִ��ʱ��Ϊ��");
						continue;
					}
					trigger = new CronTrigger(triggerName, triggerGroupName, cron);
					job.getJobDataMap().put(TaskFrameWork.MON_P_INFO, list);
					context.getScheduler().scheduleJob(job, trigger);
					if (log.isDebugEnabled()) {
						log.debug("������ȵĺϲ�����KEY:" + item);
					}
				}
			}
		} else {
			//�Ǻϲ�����
			for (int i = 0; i < objMonPInfo.length; i++) {
				long infoId = objMonPInfo[i].getInfoId();
				try {
					if (isRunning("job_" + infoId, TaskFrameWork.TASK_JOB_GROUP, runningJobs)) {
						log.info("job����:" + infoId + ",�����������У��˴β�����ش�������.�ȴ����������н���,�����´�ɨ���ʱ��,�Ż���ش�����");
					} else {
						String jobName = "job_" + infoId;
						String jobGroupName = TaskFrameWork.TASK_JOB_GROUP;
						String triggerName = "trigger_" + infoId;
						String triggerGroupName = TaskFrameWork.TASK_TRIGGER_GROUP;
						JobDetail job = new JobDetail(jobName, jobGroupName, TaskJob.class);
						Trigger trigger = null;
						String cron = objMonPInfo[i].getExpr();
						if (StringUtils.isBlank(cron)) {
							log.error("����ID:" + infoId + ",���õ�����ִ��ʱ��Ϊ��");
						} else {
							trigger = new CronTrigger(triggerName, triggerGroupName, cron);
							job.getJobDataMap().put(TaskFrameWork.MON_P_INFO, objMonPInfo[i]);
							context.getScheduler().scheduleJob(job, trigger);
							if (log.isDebugEnabled()) {
								log.debug("������ȵ�����ID:" + infoId);
							}
						}
					}
				} catch (Exception ex1) {
					log.error("����ID:" + infoId + ",�޷�����", ex1);
				}
			}
		}
	}

	/** ����JOB���ֺ������ж�JOB�Ƿ���������*/
	private static boolean isRunning(String jobName, String groupName, List runningJobs) throws Exception {
		boolean rtn = false;
		for (Iterator iter = runningJobs.iterator(); iter.hasNext();) {
			JobExecutionContext item = (JobExecutionContext) iter.next();
			if ((item.getJobDetail().getName().equalsIgnoreCase(jobName)) && (item.getJobDetail().getGroup().equalsIgnoreCase(groupName))) {
				rtn = true;
				break;
			}
		}
		return rtn;
	}

	/** ɾ��JOB*/
	private static void removeJobAndTrigger(String jobName, String groupName, JobExecutionContext context) throws Exception {
		boolean rtn = context.getScheduler().deleteJob(jobName, groupName);
		if (log.isDebugEnabled())
			if (rtn) {
				log.debug("ɾ����job����:" + jobName);
			} else {
				log.debug("job����:" + jobName + "�ڵ�������û���ҵ�,���Բ���Ҫɾ��");
			}
	}
}