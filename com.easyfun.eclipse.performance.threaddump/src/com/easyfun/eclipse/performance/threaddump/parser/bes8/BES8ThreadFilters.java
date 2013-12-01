package com.easyfun.eclipse.performance.threaddump.parser.bes8;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.easyfun.eclipse.performance.threaddump.parser.ThreadInfo;

/**
 * BES8 jvm.log
 * @author linzhaoming
 *
 * 2013-11-17
 *
 */
public class BES8ThreadFilters {
	
	public static class RunnableFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals(BES8ThreadState.STATE_RUNNABLE)){
				return true;
			}
			return false;
		}
	}
	
	public static class DefaultFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals(BES8ThreadState.STATE_UNKNOWN)){
				return true;
			}
			return false;
		}
	}
	
	public static class ObjectWaitFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals(BES8ThreadState.STATE_OBJECT_WAIT)){
				return true;
			}
			return false;
		}
	}
	
	public static class WaitingOnConidtionFilter extends ViewerFilter{
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ThreadInfo th = (ThreadInfo)element;
			if(th.getState().equals(BES8ThreadState.STATE_WAITING_ON_CONDITION)){
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
