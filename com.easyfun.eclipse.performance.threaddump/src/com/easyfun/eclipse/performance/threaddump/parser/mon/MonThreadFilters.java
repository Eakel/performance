package com.easyfun.eclipse.performance.threaddump.parser.mon;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.easyfun.eclipse.performance.threaddump.parser.ThreadInfo;

/**
 * 监控系统生成的ThreadDump
 * 
 * @author linzhaoming
 *
 * 2013-11-17
 *
 */
public class MonThreadFilters {
	
	public static class RunnableFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals(MonThreadState.STATE_RUNNABLE)){
				return true;
			}
			return false;
		}
	}
	
	public static class UnknownFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals(MonThreadState.STATE_UNKNOWN)){
				return true;
			}
			return false;
		}
	}
	
	public static class WaitingFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals(MonThreadState.STATE_OBJECT_WAIT)){
				return true;
			}
			return false;
		}
	}
	
	public static class TimedWaitingFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals(MonThreadState.STATE_WAITING_ON_CONDITION)){
				return true;
			}
			return false;
		}
	}
	
	public static class BlockedFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals(MonThreadState.STATE_BLOCK)){
				return true;
			}
			return false;
		}
	}
	
	public static class AllFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			return true;
		}
	}
}
