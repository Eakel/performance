package com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.impl.MonMergeTaskImpl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.impl.MonTaskImpl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.interfaces.ITask;

/**
 * 执行Task的JOB，调用ITask实现类的doTask(List)
 * @author linzhaoming
 * 
 * Created at 2012-9-22
 */
public class TaskJob implements Job {
	private static transient Log log = LogFactory.getLog(TaskJob.class);

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap();
		List list = (List) data.get(TaskFrameWork.MON_P_INFO);
		try {
			if (list.size() == 1) {
				ITask objTask = new MonTaskImpl();
				objTask.doTask(list);
			} else {
				ITask objTask = new MonMergeTaskImpl();
				objTask.doTask(list);
			}
		} catch (Throwable ex) {
			log.error("执行监控任务出现异常", ex);
		}
	}
}