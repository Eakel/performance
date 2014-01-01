package com.easyfun.eclipse.performance.threaddump.parser.mon;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.graphics.Image;

import com.easyfun.eclipse.performance.threaddump.ThreadDumpImageConstants;
import com.easyfun.eclipse.performance.threaddump.ThreadDumpActivator;
import com.easyfun.eclipse.performance.threaddump.parser.AbstractThreadParser;
import com.easyfun.eclipse.performance.threaddump.parser.FilterWrapper;
import com.easyfun.eclipse.performance.threaddump.parser.IThreadState;
import com.easyfun.eclipse.performance.threaddump.parser.ThreadInfo;


/**
 * 解析监控系统生成的ThreadDump
 * 
 * @author linzhaoming
 *
 * 2012-4-1
 *
 */
public class MonThreadPaser extends AbstractThreadParser{
	
	/** 标识ThreadDump开始的字符串*/
	private static final String THREAD_BEGIN_MARKER = "&quot;";
	
	protected boolean isThreadBeginMark(String line){
	//AppMonitor的情况
	//格式为：1	4682	RUNNABLE	WorkerThread#1[10.3.3.216:48402]	sun.management.ThreadImpl.getThreadInfo1(ThreadImpl.java:-2)
		line.split("");
		String[] tmps = line.split("\t");
		if(tmps.length >0){
			if(NumberUtils.isNumber(tmps[0])){
				return true;
			}
		}
		return line.trim().startsWith(THREAD_BEGIN_MARKER);
	}
	
	protected void handleThreadState(String str, ThreadInfo info) {
		info.setState(MonThreadState.getStateFromTitle(str));
	}
	
	public String getThreadName(String line) {
		String[] strs = line.split("\t");
		if(strs.length >=5){
			return strs[3];
		}else{
			return line;
		}
	}
	
	public String getThreadMethod(String line){
		String[] strs = line.split("\t");
		if(strs.length >=5){
			return strs[4];
		}else{
			return line;
		}
	}
	
	public Image getStateImage(IThreadState state) {
		return state.getImage();
	}

	public String getStateText(IThreadState state) {		
		return state.getText();
	}
	
	public String getThreadDesc(List<ThreadInfo> threadInfos){
		//线程概况
		int objectWait = 0;
		int waitingOnCondition = 0;
		int runnable = 0;
		int block = 0;
		int unkonwn = 0;
		
		for (ThreadInfo info : threadInfos) {
			IThreadState state = info.getState();
			if (state.equals(MonThreadState.STATE_RUNNABLE)) {
				runnable++;
			}else if (state.equals(MonThreadState.STATE_OBJECT_WAIT)) {
				objectWait++;
			} else if (state.equals(MonThreadState.STATE_WAITING_ON_CONDITION)) {
				waitingOnCondition++;
			} else if (state.equals(MonThreadState.STATE_BLOCK)) {
				block++;
			} else if (state.equals(MonThreadState.STATE_UNKNOWN)) {
				unkonwn++;
			}
		}

		StringBuffer sb =  new StringBuffer("线程总数: " + threadInfos.size());
		sb.append("  RUNNABLE: " + runnable);
		sb.append("  WAITING: " + objectWait);
		sb.append("  TIMED_WAITING: " + waitingOnCondition);		
		sb.append("  BLOCKED: " + block);
		if(unkonwn >0){
			sb.append("  未知: " + unkonwn);
		}
		
		return sb.toString();
	}
	
	public HashMap<String, FilterWrapper> getAllFilterMap(){
		HashMap<String, FilterWrapper> map = new HashMap<String, FilterWrapper>();
		map.put("ALL", new FilterWrapper(new ViewerFilter[] { new MonThreadFilters.AllFilter() }, null));
		map.put("WAITING", new FilterWrapper(new ViewerFilter[] { new MonThreadFilters.WaitingFilter() }, null));
		map.put("&TIME_WAITING", new FilterWrapper(new ViewerFilter[] { new MonThreadFilters.TimedWaitingFilter() }, ThreadDumpActivator.getImageDescriptor(
				ThreadDumpImageConstants.ICON_THREAD_WAIT_ON_CONDITION_PATH).createImage()));
		map.put("&RUNNABLE", new FilterWrapper(new ViewerFilter[] { new MonThreadFilters.RunnableFilter() }, ThreadDumpActivator.getImageDescriptor(
				ThreadDumpImageConstants.ICON_THREAD_DEFAULT_PATH).createImage()));
		map.put("BLOCKED", new FilterWrapper(new ViewerFilter[] { new MonThreadFilters.BlockedFilter() }, ThreadDumpActivator.getImageDescriptor(
				ThreadDumpImageConstants.ICON_THREAD_WAITING_PATH).createImage())); //TODO:
		return map;
	}
}
