package com.easyfun.eclipse.performance.navigator.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.easyfun.eclipse.performance.ImageConstants;
import com.easyfun.eclipse.performance.PerformanceActivator;
import com.easyfun.eclipse.performance.navigator.ItemNavigationView;
import com.easyfun.eclipse.rcp.IDEHelper;

/**
 * ´ò¿ª¹Ø±ÕNavigator
 * @author zhaoming
 * 
 * Dec 27, 2007
 */
public class ShowHideNavigatorViewAction extends Action implements IWorkbenchAction{
	private boolean isOpened = true;

	public ShowHideNavigatorViewAction() {
		setText("Navigator");
		setToolTipText("Show/Hide Navigator");
		setId(getText());
		setImageDescriptor(PerformanceActivator.getImageDescriptor(ImageConstants.ICON_EASYFUN_PATH));
	}

	public void dispose() {
	}

	public void run() {
		IWorkbenchPage page = IDEHelper.getActivePage();
		try {
			isOpened = !isOpened;
			if (isOpened) {
//				setText("Close Navigator");
				page.showView(ItemNavigationView.VIEW_ID);
			} else {
//				setText("Open Navigator");
				IViewReference ref = page.findViewReference(ItemNavigationView.VIEW_ID);
				page.hideView(ref);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
