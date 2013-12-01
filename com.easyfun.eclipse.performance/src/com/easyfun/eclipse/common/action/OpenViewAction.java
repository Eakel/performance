package com.easyfun.eclipse.common.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

/**
 * ��ָ����View ID
 * @author linzhaoming
 *
 * 2012-4-5
 *
 */
public class OpenViewAction extends Action {

	private final IWorkbenchWindow window;
	private int instanceNum = 0;
	private final String viewId;
	
	/** �Ƿ���Ҫ��ʾ�������*/
	private boolean multi = false;
	
	/**
	 * 
	 * @param window
	 * @param label ��ʾ��Text
	 * @param commandId Command ID,��Ҫ��plugin.xml�ж���Command
	 * @param viewId View ID
	 * @param needImage �Ƿ���Ҫ��ʾͼ��
	 */
	public OpenViewAction(IWorkbenchWindow window, String label, String commandId, String viewId, boolean needImage) {
		this.window = window;
		this.viewId = viewId;
		setText(label);
		// The id is used to refer to the action in a menu or toolbar
		setId(commandId);
		// Associate the action with a pre-defined command, to allow key bindings.
		setActionDefinitionId(commandId);
		if(needImage){
			setImageDescriptor(com.easyfun.eclipse.performance.PerformanceActivator.getImageDescriptor("/icons/sample2.gif"));
		}
	}
	
	public void setNeedMulti(boolean multi){
		this.multi = multi;
	}

	public void run() {
		if (window != null) {
			try {
				
				if(multi){
					window.getActivePage().showView(viewId, Integer.toString(instanceNum++), IWorkbenchPage.VIEW_ACTIVATE);
				}else{
					window.getActivePage().showView(viewId, null, IWorkbenchPage.VIEW_ACTIVATE);	
				}
//				window.getActivePage().showView(viewId);
			} catch (PartInitException e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}
}
