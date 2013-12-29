package com.easyfun.eclipse.performance.navigator.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.easyfun.eclipse.performance.ImageConstants;
import com.easyfun.eclipse.performance.PerformanceActivator;
import com.easyfun.eclipse.performance.helper.EasyFunUtils;
import com.easyfun.eclipse.performance.navigator.WelcomeView;

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
        for (IViewReference iViewReference: iViewReferences) {
        	if(!EasyFunUtils.getUncontrolViews().contains(iViewReference.getId())){
        		 page.hideView(iViewReference); 
        	}
        }
        
        //显示WelcomeView
		try {
			page.showView(WelcomeView.VIEW_ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	public void dispose() {
	}
}
