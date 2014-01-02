package com.easyfun.eclipse.performance.appframe.monitor.mon.exe.task.javacommand;

/**
 * 有3个实现类
 * <li>OracleActionCommand
 * <li>OracleAWRCommand 收集Oracle的AWR日志
 * <li>TelnetPort
 * @author linzhaoming
 * 
 * Created at 2012-9-22
 */
public interface IJavaCommand {
	public String execute(String in) throws Exception;
}