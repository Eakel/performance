package com.easyfun.eclipse.performance.threaddump.parser.sun;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.graphics.Image;

import com.easyfun.eclipse.performance.threaddump.ImageConstants;
import com.easyfun.eclipse.performance.threaddump.ThreadDumpActivator;
import com.easyfun.eclipse.performance.threaddump.parser.AbstractThreadParser;
import com.easyfun.eclipse.performance.threaddump.parser.FilterWrapper;
import com.easyfun.eclipse.performance.threaddump.parser.IThreadState;
import com.easyfun.eclipse.performance.threaddump.parser.ThreadInfo;
import com.easyfun.eclipse.utils.lang.StringUtil;

/**
 * 解析WebLogic的ThreadDump
 * 
 * @author linzhaoming
 *
 * 2012-4-1
 *
 */
public class SunThreadPaser extends AbstractThreadParser{
	/** 标识ThreadDump开始的字符串*/
	private static final String THREAD_BEGIN_MARKER = "\"";
	
	protected boolean isThreadBeginMark(String line){
		return line.trim().startsWith(THREAD_BEGIN_MARKER);
	}
	
	protected void handleThreadState(String str, ThreadInfo info) {
		info.setState(SunThreadState.getStateFromTitle(str));
	}
	
	public String getThreadName(String line){
		return StringUtil.substringBetween(line, "\"");
	}
	
	public String getThreadMethod(String line){
		return "";
	}
	
	public Image getStateImage(IThreadState state) {
		return state.getImage();
	}

	public String getStateText(IThreadState state) {
		return "";
	}
	
	public String getThreadDesc(List<ThreadInfo> threadInfos){
		//线程概况
		int size1 = 0;
		int size2 = 0;
		int size3 = 0;
		int size4 = 0;
		
		for (ThreadInfo info : threadInfos) {
			IThreadState state = info.getState(); 
			if(state.equals(SunThreadState.STATE_UNKNOWN)){
				size1++;
			}else if(state.equals(SunThreadState.STATE_OBJECT_WAIT)){
				size2++;
			}else if(state.equals(SunThreadState.STATE_WAITING_ON_CONDITION)){
				size4++;
			}else if(state.equals(SunThreadState.STATE_RUNNABLE)){
				size3++;
			}
		}

		StringBuffer sb =  new StringBuffer("线程总数: " + threadInfos.size());
		sb.append("  Default: " + size1);
		sb.append("  Wait: " + size2);
		sb.append("  Runnable: " + size3);
		sb.append("  WaitOnCondition: " + size4);
		
		return sb.toString();
	}
	
	public HashMap<String, FilterWrapper> getAllFilterMap(){
		HashMap<String, FilterWrapper> map = new HashMap<String, FilterWrapper>();
		map.put("All", new FilterWrapper(new ViewerFilter[] { new SunThreadFilters.AllFilter() }, null));
		map.put("&Runnable", new FilterWrapper(new ViewerFilter[] { new SunThreadFilters.RunnableFilter() }, ThreadDumpActivator.getImageDescriptor(
				ImageConstants.ICON_THREAD_RUNNABLE).createImage()));
		map.put("&Default", new FilterWrapper(new ViewerFilter[] { new SunThreadFilters.DefaultFilter() }, ThreadDumpActivator.getImageDescriptor(
				ImageConstants.ICON_THREAD_DEFAULT).createImage()));
		map.put("ObjectWait", new FilterWrapper(new ViewerFilter[] { new SunThreadFilters.ObjectWaitFilter() }, ThreadDumpActivator.getImageDescriptor(
				ImageConstants.ICON_THREAD_WAITING).createImage()));
		map.put("WaitingOnConidtion", new FilterWrapper(new ViewerFilter[] { new SunThreadFilters.WaitingOnConidtionFilter() }, ThreadDumpActivator
				.getImageDescriptor(ImageConstants.ICON_THREAD_WAIT_ON_CONDITION).createImage()));
		return map;
	}
}
