package com.easyfun.eclipse.performance.threaddump.parser.webloigc;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.easyfun.eclipse.performance.threaddump.parser.ThreadInfo;

/**
 * WebLogic�������ɵ�ThreadDump
 * 
 * @author linzhaoming
 *
 * 2013-11-17
 *
 */
public class WeblogicThreadFilters {
	
	public static class RunnableFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals(WebLogicThreadState.STATE_RUNNABLE)){
				return true;
			}
			return false;
		}
	}
	
	public static class DefaultFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals( WebLogicThreadState.STATE_UNKNOWN)){
				return true;
			}
			return false;
		}
	}
	
	public static class ObjectWaitFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals(WebLogicThreadState.STATE_OBJECT_WAIT)){
				return true;
			}
			return false;
		}
	}
	
	public static class WaitingOnConidtionFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals(WebLogicThreadState.STATE_WAITING_ON_CONDITION)){
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
