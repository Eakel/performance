package com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.interfaces;

import java.util.List;

/**
 * ������ʵ����
 * <li>MonMergeTaskImpl ���
 * <li>MonTaskImpl ����
 * @author linzhaoming
 * 
 * Created at 2012-9-22
 */
public interface ITask {
	/** ִ�����񣬲���������浽��[MON_L_RECORD]�У������·ֱ�*/
	public void doTask(List in) throws Exception;
}