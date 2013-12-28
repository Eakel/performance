package com.easyfun.eclipse.common.navigator.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

/**
 * 打开指定的View ID
 * @author linzhaoming
 *
 * 2012-4-5
 *
 */
public class OpenViewAction extends Action {

	private final IWorkbenchWindow window;
	private int instanceNum = 0;
	private final String viewId;
	
	/** 是否需要显示多个窗口*/
	private boolean multi = false;
	
	/**
	 * 
	 * @param window
	 * @param label 显示的Text
	 * @param commandId Command ID,需要在plugin.xml中定义Command
	 * @param viewId View ID
	 * @param needImage 是否需要显示图标
	 */
	public OpenViewAction(IWorkbenchWindow window, String label, String commandId, String viewId, boolean needImage) {
		this.window = window;
		this.viewId = viewId;
		setText(label);
		// The id is used to refer to the action in a menu or toolbar
		setId(commandId);
		// Associate the action with a pre-defined command, to allow key bindings.
		setActionDefinitionId(commandId);
		setToolTipText("Show/Hide " + label);
		if(needImage){
			setImageDescriptor(com.easyfun.eclipse.performance.PerformanceActivator.getImageDescriptor("/icons/sample2.gif"));
		}
	}
	
	public void setNeedMulti(boolean multi){
		this.multi = multi;
	}
	
	public void setViewImageDescriptor(String imageDesc){
		setImageDescriptor(com.easyfun.eclipse.performance.PerformanceActivator.getImageDescriptor(imageDesc));
	}

	public void run() {
		IWorkbenchPage page = window.getActivePage();
		IViewPart viewPart = page.findView(viewId);
		boolean visible = page.isPartVisible(viewPart);
		if (window != null) {
			try {
				if(multi){
					//显示多个不用处理
					page.showView(viewId, Integer.toString(instanceNum++), IWorkbenchPage.VIEW_ACTIVATE);
				}else{
					if(visible){
						page.hideView(viewPart);
					}else{
						page.showView(viewId, null, IWorkbenchPage.VIEW_ACTIVATE);
					}
				}
			} catch (PartInitException e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}
}
