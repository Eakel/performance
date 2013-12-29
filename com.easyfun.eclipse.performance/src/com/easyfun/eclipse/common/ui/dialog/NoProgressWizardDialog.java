package com.easyfun.eclipse.common.ui.dialog;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * The <code>WizardDialog</code> which make progress indicator zero height.
 * <p> Could be used the same with <code>WizardDialog</code> if you want this behavior.
 * 
 * @author linzhaoming
 *
 */
public class NoProgressWizardDialog extends WizardDialog {
	public NoProgressWizardDialog(Shell parentShell, IWizard newWizard){
		super(parentShell, newWizard);
	}
	
	protected Control createDialogArea(Composite parent) {
		Control control= super.createDialogArea(parent);
		if(!getWizard().needsProgressMonitor()){
			IProgressMonitor monitor=getProgressMonitor();
			if(monitor instanceof Control){
				Object layoutData=((Control)monitor).getLayoutData();
				if(layoutData instanceof GridData){
					((GridData)layoutData).heightHint=0;
				}
			}
		}
		return control;
	}
	
	
}
