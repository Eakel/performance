package com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.interfaces;

import java.util.List;

/**
 * 有两个实现类
 * <li>MonMergeTaskImpl 多个
 * <li>MonTaskImpl 单个
 * @author linzhaoming
 * 
 * Created at 2012-9-22
 */
public interface ITask {
	/** 执行任务，并将结果保存到表[MON_L_RECORD]中，按年月分表*/
	public void doTask(List in) throws Exception;
}