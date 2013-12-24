package com.easyfun.eclipse.common.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.easyfun.eclipse.common.view.item.navigator.ItemNavigationView;
import com.easyfun.eclipse.performance.PerformanceActivator;
import com.easyfun.eclipse.utils.common.ImageConstants;

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
		setImageDescriptor(PerformanceActivator.getImageDescriptor(ImageConstants.EASYFUN_ICONS));
	}

	public void dispose() {
	}

	public void run() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
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
