package com.easyfun.eclipse.common.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.easyfun.eclipse.common.UIConstants;
import com.easyfun.eclipse.performance.PerformanceActivator;

/**
 * �򿪹ر�Navigator
 * @author zhaoming
 * 
 * Dec 27, 2007
 */
public class ShowHideNavigatorViewAction extends Action implements IWorkbenchAction{
	private boolean isOpened = true;

	public ShowHideNavigatorViewAction() {
		setText("Close Navigator");
		setId(getText());
		setImageDescriptor(PerformanceActivator.getImageDescriptor("icons/esayfun.png"));
	}

	public void dispose() {
	}

	public void run() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			isOpened = !isOpened;
			if (isOpened) {
				setText("Close Navigator");
				page.showView(UIConstants.VIEWID_NAVIGATION);
			} else {
				setText("Open Navigator");
				IViewReference ref = page.findViewReference(UIConstants.VIEWID_NAVIGATION);
				page.hideView(ref);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
