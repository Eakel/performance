package com.easyfun.eclipse.performance.appframe.charset;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * ×ª»»±àÂë
 * 
 * @author linzhaoming
 *
 * 2011-1-9
 *
 */
public class UTF8CharsetAction implements IObjectActionDelegate {
	private Shell shell = null;
	private IStructuredSelection selection;
	
	IWorkbenchPart part;
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		part = targetPart;
		shell = targetPart.getSite().getShell();
	}

	public void run(IAction action) {
		ContentEncodingJob job = new ContentEncodingJob(this.selection, "UTF-8");
		job.schedule();
	}
	
	public void selectionChanged(IAction action, ISelection selection) {
		if(selection != null && selection instanceof IStructuredSelection){
			this.selection = (IStructuredSelection) selection;
			action.setEnabled(!this.selection.isEmpty());
		}
	}
}