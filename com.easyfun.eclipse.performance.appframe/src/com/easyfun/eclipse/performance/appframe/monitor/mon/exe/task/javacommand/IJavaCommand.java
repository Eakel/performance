package com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.javacommand;

/**
 * ��3��ʵ����
 * <li>OracleActionCommand
 * <li>OracleAWRCommand �ռ�Oracle��AWR��־
 * <li>TelnetPort
 * @author linzhaoming
 * 
 * Created at 2012-9-22
 */
public interface IJavaCommand {
	public String execute(String in) throws Exception;
}