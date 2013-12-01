package com.easyfun.eclipse.performance.jmx.dialog;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author linzhaoming Create Date: 2010-8-14
 */
public class MBeanOperationDialog extends Dialog {
	
	private MBeanOperationInfo operationInfo;
	
	public MBeanOperationDialog(Shell shell, MBeanOperationInfo operationInfo) {
		super(shell);
		this.operationInfo = operationInfo;
	}
	
	protected Control createDialogArea(Composite parent) {		
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout());
		
		Label label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("Hell");
		
		Composite paramComposite = new Composite(composite, SWT.NULL);
		paramComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		paramComposite.setLayout(new GridLayout(4, false));
		MBeanParameterInfo[] parameterInfos = operationInfo.getSignature();
		for (int i = 0; i < parameterInfos.length; i++) {
			MBeanParameterInfo paramInfo = parameterInfos[i];
			paramInfo.getName();
			paramInfo.getType();
			paramInfo.getDescription();
			
			Label label1 = new Label(paramComposite, SWT.NULL);
			label1.setLayoutData(new GridData());
			label1.setText(paramInfo.getName());
			
			label1 = new Label(paramComposite, SWT.NULL);
			label1.setLayoutData(new GridData());
			label1.setText(paramInfo.getType());
			
			label1 = new Label(paramComposite, SWT.NULL);
			label1.setLayoutData(new GridData());
			label1.setText("");
			
			label1 = new Label(paramComposite, SWT.NULL);
			label1.setLayoutData(new GridData());
			label1.setText(paramInfo.getDescription());
		}
		return composite;
	}
}
