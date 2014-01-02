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
 * Task的JOB实现类
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
			log.error("执行扫描任务出现异常", ex);
		}
	}

	private void _execute(JobExecutionContext context) throws Exception {
		log.info("扫描任务执行");
		IMonSV objITaskSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		MonPInfo[] objMonPInfo = objITaskSV.getAllMonPInfo();

		if ((objMonPInfo == null) || (objMonPInfo.length == 0)) {
			log.info("扫描到符合要求的任务为零,这次不处理");
			return;
		}

		List runningJobs = context.getScheduler().getCurrentlyExecutingJobs();
		if (log.isDebugEnabled()) {
			for (Iterator iter = runningJobs.iterator(); iter.hasNext();) {
				JobExecutionContext item = (JobExecutionContext) iter.next();
				if ((item.getJobInstance() instanceof TaskJob)) {
					Object obj = item.getJobDetail().getJobDataMap().get(TaskFrameWork.MON_P_INFO);
					if(obj instanceof MonPInfo){
						log.debug("系统中还有未完成的任务,任务ID:" + ((MonPInfo) obj).getInfoId() + ",执行类:" + item.getJobDetail().getJobClass().getName());
					}else if(obj instanceof List){
						log.debug("系统中还有未完成的任务(List),大小为:" + ((List)obj).size());
						for (Object t : (List)obj) {
							if(t instanceof MonPInfo){
								log.debug("系统中还有未完成的任务,任务ID:" + ((MonPInfo) t).getInfoId() + ",执行类:" + item.getJobDetail().getJobClass().getName());
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
					log.info("job名称:" + jobNames[i] + ",任务正在运行，此次不会加载此条数据.等待此任务运行结束,并且下次扫描的时候,才会加载此数据");
				} else {
					try {
						removeJobAndTrigger(jobNames[i], TaskFrameWork.TASK_JOB_GROUP, context);
					} catch (Exception ex2) {
						log.info("job名称:" + jobNames[i] + ",从调度器中删除失败,现在不删除,下次删除", ex2);
					}
				}
			}
		
		HashMap map;	//key为任务名称，value为List<MonPInfo>
		if (TaskFrameWork.isMergeExecTask()) {
			//合并任务，监控的实际配置
			map = new HashMap();

			for (int i = 0; i < objMonPInfo.length; i++) {
				long infoId = objMonPInfo[i].getInfoId();
				try {
					String key = null;
					//若为EXEC任务， 以[HOSTNAME]^[TIME_ID]^[TYPE]作为KEY，否则以[INFO_ID]作为KEY
					if ((!StringUtils.isBlank(objMonPInfo[i].getHostname())) && (objMonPInfo[i].getTimeId() > 0L) && (objMonPInfo[i].getType().equals("EXEC"))) {
						key = objMonPInfo[i].getHostname() + "^" + objMonPInfo[i].getTimeId() + "^" + objMonPInfo[i].getType();
					} else {
						key = String.valueOf(infoId);
					}

					if (isRunning("job_" + key, TaskFrameWork.TASK_JOB_GROUP, runningJobs)) {
						log.info("job名称:" + key + ",任务正在运行，此次不会加载此条数据.等待此任务运行结束,并且下次扫描的时候,才会加载此数据");
					} else if (map.containsKey(key)) {
						List list = (List) map.get(key);
						list.add(objMonPInfo[i]);
					} else {
						List list = new ArrayList();
						list.add(objMonPInfo[i]);
						map.put(key, list);
					}
				} catch (Exception ex) {
					log.error("任务ID:" + infoId + ",无法调度", ex);
				}
			}

			Set key = map.keySet();
			for (Iterator iter = key.iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				List list = (List) map.get(item);
				if (list.size() == 1) {	//任务只有一个
					MonPInfo obj = (MonPInfo) list.get(0);
					String jobName = "job_" + obj.getInfoId();
					String jobGroupName = TaskFrameWork.TASK_JOB_GROUP;
					String triggerName = "trigger_" + obj.getInfoId();
					String triggerGroupName = TaskFrameWork.TASK_TRIGGER_GROUP;
					JobDetail job = new JobDetail(jobName, jobGroupName, TaskJob.class);
					Trigger trigger = null;
					String cron = obj.getExpr();
					if (StringUtils.isBlank(cron)) {
						log.error("任务ID:" + obj.getInfoId() + ",配置的周期执行时间为空");
						continue;
					}
					trigger = new CronTrigger(triggerName, triggerGroupName, cron);
					job.getJobDataMap().put(TaskFrameWork.MON_P_INFO, list);
					context.getScheduler().scheduleJob(job, trigger);
					if (log.isDebugEnabled()) {
						log.debug("加入调度的任务ID:" + obj.getInfoId());
					}
				} else {	//任务有多个
					MonPInfo obj = (MonPInfo) list.get(0);
					String jobName = "job_" + item;
					String jobGroupName = TaskFrameWork.TASK_JOB_GROUP;
					String triggerName = "trigger_" + item;
					String triggerGroupName = TaskFrameWork.TASK_TRIGGER_GROUP;
					JobDetail job = new JobDetail(jobName, jobGroupName, TaskJob.class);

					Trigger trigger = null;
					String cron = obj.getExpr();
					if (StringUtils.isBlank(cron)) {
						log.error("合并任务KEY:" + obj.getInfoId() + ",配置的周期执行时间为空");
						continue;
					}
					trigger = new CronTrigger(triggerName, triggerGroupName, cron);
					job.getJobDataMap().put(TaskFrameWork.MON_P_INFO, list);
					context.getScheduler().scheduleJob(job, trigger);
					if (log.isDebugEnabled()) {
						log.debug("加入调度的合并任务KEY:" + item);
					}
				}
			}
		} else {
			//非合并任务
			for (int i = 0; i < objMonPInfo.length; i++) {
				long infoId = objMonPInfo[i].getInfoId();
				try {
					if (isRunning("job_" + infoId, TaskFrameWork.TASK_JOB_GROUP, runningJobs)) {
						log.info("job名称:" + infoId + ",任务正在运行，此次不会加载此条数据.等待此任务运行结束,并且下次扫描的时候,才会加载此数据");
					} else {
						String jobName = "job_" + infoId;
						String jobGroupName = TaskFrameWork.TASK_JOB_GROUP;
						String triggerName = "trigger_" + infoId;
						String triggerGroupName = TaskFrameWork.TASK_TRIGGER_GROUP;
						JobDetail job = new JobDetail(jobName, jobGroupName, TaskJob.class);
						Trigger trigger = null;
						String cron = objMonPInfo[i].getExpr();
						if (StringUtils.isBlank(cron)) {
							log.error("任务ID:" + infoId + ",配置的周期执行时间为空");
						} else {
							trigger = new CronTrigger(triggerName, triggerGroupName, cron);
							job.getJobDataMap().put(TaskFrameWork.MON_P_INFO, objMonPInfo[i]);
							context.getScheduler().scheduleJob(job, trigger);
							if (log.isDebugEnabled()) {
								log.debug("加入调度的任务ID:" + infoId);
							}
						}
					}
				} catch (Exception ex1) {
					log.error("任务ID:" + infoId + ",无法调度", ex1);
				}
			}
		}
	}

	/** 根据JOB名字和组名判断JOB是否正在运行*/
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

	/** 删除JOB*/
	private static void removeJobAndTrigger(String jobName, String groupName, JobExecutionContext context) throws Exception {
		boolean rtn = context.getScheduler().deleteJob(jobName, groupName);
		if (log.isDebugEnabled())
			if (rtn) {
				log.debug("删除的job名称:" + jobName);
			} else {
				log.debug("job名称:" + jobName + "在调度器中没有找到,所以不需要删除");
			}
	}
}