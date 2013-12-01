//package com.easyfun.eclipse.performance.threaddump;
//
//import org.eclipse.jface.viewers.Viewer;
//import org.eclipse.jface.viewers.ViewerFilter;
//import org.eclipse.swt.graphics.Image;
//
//import com.easyfun.eclipse.performance.threaddump.parser.ThreadInfo;
//
//public class ThreadFilters {
//	
//	public static class FilterWrapper {
//		private ViewerFilter[] filters;
//		private Image image;
//
//		public FilterWrapper(ViewerFilter[] filters, Image image) {
//			this.filters = filters;
//			this.image = image;
//		}
//
//		public ViewerFilter[] getFilters() {
//			return filters;
//		}
//
//		public Image getImage() {
//			return image;
//		}
//	}
//	
//	public static class RunnableFilter extends ViewerFilter{
//		public boolean select(Viewer viewer, Object parentElement, Object element) {
//			ThreadInfo th = (ThreadInfo)element;
//			if(th.getState() == ThreadState.STATE_RUNNABLE){
//				return true;
//			}
//			return false;
//		}
//	}
//	
//	public static class DefaultFilter extends ViewerFilter{
//		public boolean select(Viewer viewer, Object parentElement, Object element) {
//			ThreadInfo th = (ThreadInfo)element;
//			if(th.getState() == ThreadState.STATE_DEFAULT){
//				return true;
//			}
//			return false;
//		}
//	}
//	
//	public static class ObjectWaitFilter extends ViewerFilter{
//		public boolean select(Viewer viewer, Object parentElement, Object element) {
//			ThreadInfo th = (ThreadInfo)element;
//			if(th.getState() == ThreadState.STATE_OBJECT_WAIT){
//				return true;
//			}
//			return false;
//		}
//	}
//	
//	public static class WaitingOnConidtionFilter extends ViewerFilter{
//		public boolean select(Viewer viewer, Object parentElement, Object element) {
//			ThreadInfo th = (ThreadInfo)element;
//			if(th.getState() == ThreadState.STATE_WAITING_ON_CONDITION){
//				return true;
//			}
//			return false;
//		}
//	}
//	
//	public static class AllFilter extends ViewerFilter{
//		public boolean select(Viewer viewer, Object parentElement,
//				Object element) {
//			return true;
//		}
//	}
//}
