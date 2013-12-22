package com.easyfun.eclipse.performance.appframe.promote;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class PromoteAction implements IObjectActionDelegate {
	private Shell shell;
	private ISelection selection;
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	public void run(IAction action) {
		if(selection!=null && selection instanceof IStructuredSelection){
			IStructuredSelection structSelection = (IStructuredSelection)(selection);
			Iterator<?> iter = structSelection.iterator();
			while(iter.hasNext()){
				IFile file = (IFile)iter.next();
				showMessage(file.getLocation().toPortableString());
			}
		}
	}
	
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection=selection;
	}

	private void showMessage(String msg){
		MessageDialog.openInformation(shell, "EasyFun", msg);
	}
}