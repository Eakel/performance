package com.easyfun.eclipse.common.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.easyfun.eclipse.common.view.item.navigator.ItemNavigationView;
import com.easyfun.eclipse.performance.PerformanceActivator;
import com.easyfun.eclipse.utils.common.ImageConstants;

/**
 * 关闭所有的View
 * 
 * @author linzhaoming
 *
 * 2011-5-7
 *
 */
public class CloseAllAction  extends Action implements IWorkbenchAction{
	public CloseAllAction(){
		super("Close All");
		setId("com.easyfun.eclipse.common.action.closeAll");
		setImageDescriptor(PerformanceActivator.getImageDescriptor(ImageConstants.CLOSEALL_ICONS));
	}
	
	public void run() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewReference[] iViewReferences = page.getViewReferences();
        for (IViewReference iViewReference: iViewReferences)
             if (!ItemNavigationView.VIEW_ID.equals(iViewReference.getId())){
                  page.hideView(iViewReference); 
             }
	}
	
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
