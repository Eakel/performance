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
 * �����ļ�task.properties����Ҫ������quartz������
 * @author linzhaoming
 * 
 * Created at 2012-9-17
 */
public class TaskFrameWork {
	private static transient Log log = LogFactory.getLog(TaskFrameWork.class);
	
	/** ����ID��KEY������List&ltMonPInfo>*/
	public static final String MON_P_INFO = "MON_P_INFO";
	
	/** Task��������*/
	public static final String TASK_JOB_GROUP = "TASK_JOB_GROUP";
	
	/** Trigger��������*/
	public static final String TASK_TRIGGER_GROUP = "TASK_TRIGGER_GROUP";
	
	/** ���ò��úϲ�����
	 * <li>Ĭ��Ϊfalse��JVM����"mon.is_merge_exec"Ϊ1����'Y'ʱΪtrue
	 * */
	private static boolean IS_MERGE_EXEC = false;

	static {
		String str = System.getProperty("mon.is_merge_exec");
		if ((!StringUtils.isBlank(str)) && ((str.trim().equalsIgnoreCase("1")) || (str.trim().equalsIgnoreCase("Y")))) {
			IS_MERGE_EXEC = true;
			log.info("���ò��úϲ�����");
		}
	}

	/** ���ò��úϲ�����
	 * <li>Ĭ��Ϊfalse��JVM����"mon.is_merge_exec"Ϊ1����'Y'ʱΪtrue
	 * */
	public static boolean isMergeExecTask() {
		return IS_MERGE_EXEC;
	}

	public static void main(String[] args) throws Exception {
		log.info("����:������һ���߳���ʹ�õ����ݿ����ӿ��ܳ���1�������������õ����ӳص��������Ϊ�߳�������1.5��!");
		
		long bootPauseSeconds = 3;		//������ͣ��ʱ��Ĭ��Ϊ3��
		String scanDataJobCron = "0 0/5 * * * ?";	//ÿ��5���Ӻ�ĵ�30�뿪ʼ����

		Properties prop = Resource.loadPropertiesFromClassPath("task.properties", "task", true);
		if ((!StringUtils.isBlank(prop.getProperty("bootPauseSeconds"))) && (StringUtils.isNumeric(prop.getProperty("bootPauseSeconds")))) {
			bootPauseSeconds = Long.parseLong(prop.getProperty("bootPauseSeconds").trim());
		}

		if (!StringUtils.isBlank(prop.getProperty("scanDataJobCron"))) {
			scanDataJobCron = prop.getProperty("scanDataJobCron").trim();
		} else {
			log.info("û������scanDataJobCron����,ȡĬ�ϵ�����:" + scanDataJobCron);
		}

		log.info("������ͣʱ��Ϊ:" + bootPauseSeconds + "��");
		log.info("scanDataJobCron��������Ϊ:" + scanDataJobCron);

		SchedulerFactory objSchedulerFactory = new StdSchedulerFactory(prop);
		Scheduler objScheduler = objSchedulerFactory.getScheduler();
		try {
			JobDetail job = new JobDetail("TaskScanJob", "TaskScanJobGrp", TaskScanJob.class);
			Trigger trigger = new CronTrigger("TaskScanTrigger", "TaskScanTriggerGrp", scanDataJobCron);
			objScheduler.scheduleJob(job, trigger);
		} catch (Exception ex) {
			log.error("ɨ����ҵ����ʧ��,ϵͳ�˳�", ex);
			System.exit(-1);
		}

		log.info("��ʼ��������ϵͳ��������");
		objScheduler.start();

		Thread.currentThread();
		Thread.sleep(bootPauseSeconds);
		log.info("��������ϵͳ���");
	}

}