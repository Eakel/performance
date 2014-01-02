package com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.Resource;

/**
 * 配置文件task.properties，主要是配置quartz的属性
 * @author linzhaoming
 * 
 * Created at 2012-9-17
 */
public class TaskFrameWork {
	private static transient Log log = LogFactory.getLog(TaskFrameWork.class);
	
	/** 任务ID的KEY，放置List&ltMonPInfo>*/
	public static final String MON_P_INFO = "MON_P_INFO";
	
	/** Task的组名称*/
	public static final String TASK_JOB_GROUP = "TASK_JOB_GROUP";
	
	/** Trigger的组名称*/
	public static final String TASK_TRIGGER_GROUP = "TASK_TRIGGER_GROUP";
	
	/** 配置采用合并任务
	 * <li>默认为false，JVM配置"mon.is_merge_exec"为1或者'Y'时为true
	 * */
	private static boolean IS_MERGE_EXEC = false;

	static {
		String str = System.getProperty("mon.is_merge_exec");
		if ((!StringUtils.isBlank(str)) && ((str.trim().equalsIgnoreCase("1")) || (str.trim().equalsIgnoreCase("Y")))) {
			IS_MERGE_EXEC = true;
			log.info("配置采用合并任务");
		}
	}

	/** 配置采用合并任务
	 * <li>默认为false，JVM配置"mon.is_merge_exec"为1或者'Y'时为true
	 * */
	public static boolean isMergeExecTask() {
		return IS_MERGE_EXEC;
	}

	public static void main(String[] args) throws Exception {
		log.info("提醒:由于在一个线程中使用的数据库连接可能超过1个，请至少配置的连接池的最大数量为线程数量的1.5倍!");
		
		long bootPauseSeconds = 3;		//启动暂停延时，默认为3秒
		String scanDataJobCron = "0 0/5 * * * ?";	//每隔5分钟后的第30秒开始触发

		Properties prop = Resource.loadPropertiesFromClassPath("task.properties", "task", true);
		if ((!StringUtils.isBlank(prop.getProperty("bootPauseSeconds"))) && (StringUtils.isNumeric(prop.getProperty("bootPauseSeconds")))) {
			bootPauseSeconds = Long.parseLong(prop.getProperty("bootPauseSeconds").trim());
		}

		if (!StringUtils.isBlank(prop.getProperty("scanDataJobCron"))) {
			scanDataJobCron = prop.getProperty("scanDataJobCron").trim();
		} else {
			log.info("没有配置scanDataJobCron参数,取默认的配置:" + scanDataJobCron);
		}

		log.info("启动暂停时间为:" + bootPauseSeconds + "秒");
		log.info("scanDataJobCron参数配置为:" + scanDataJobCron);

		SchedulerFactory objSchedulerFactory = new StdSchedulerFactory(prop);
		Scheduler objScheduler = objSchedulerFactory.getScheduler();
		try {
			JobDetail job = new JobDetail("TaskScanJob", "TaskScanJobGrp", TaskScanJob.class);
			Trigger trigger = new CronTrigger("TaskScanTrigger", "TaskScanTriggerGrp", scanDataJobCron);
			objScheduler.scheduleJob(job, trigger);
		} catch (Exception ex) {
			log.error("扫描作业加入失败,系统退出", ex);
			System.exit(-1);
		}

		log.info("开始启动调度系统…………");
		objScheduler.start();

		Thread.currentThread();
		Thread.sleep(bootPauseSeconds);
		log.info("启动调度系统完成");
	}

}