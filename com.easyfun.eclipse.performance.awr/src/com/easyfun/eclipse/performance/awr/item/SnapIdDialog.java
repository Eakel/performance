package com.easyfun.eclipse.performance.awr.item;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.util.lang.StringUtil;
import com.easyfun.eclipse.util.ui.DialogUtils;

/**
 * Dialog for SnapId input
 * 
 * @author linzhaoming
 *
 * 2011-4-2
 *
 */
public class SnapIdDialog extends Dialog {

	private Text beginIdText;
	private Text endIdText;
	
	private SnapBean snapBean;
	
	public SnapIdDialog(Shell parentShell, SnapBean snapBean) {
		super(parentShell);
		this.snapBean = snapBean;
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		container.setLayout(gridLayout);
		Label label = new Label(container, SWT.NONE);
		GridData gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		label.setLayoutData(gridData);
		label.setText("Begin Snap Id：");


		beginIdText = new Text(container, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.horizontalSpan =3;
		beginIdText.setLayoutData(gridData);
		beginIdText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
//				updateUI();
			}
		});
		beginIdText.setText("linzm");

		label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label.setText("End Snap Id：");

		endIdText = new Text(container, SWT.PASSWORD);
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		gridData.horizontalSpan =3;
		endIdText.setLayoutData(gridData);
		endIdText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
//				updateUI();
			}
		});
		
		initialize();
		
		return container;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	protected Point getInitialSize() {
		return new Point(320, 250);
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Snap信息");
	}
	
	private void initialize(){
		beginIdText.setText(String.valueOf(snapBean.getBeginId()));
		endIdText.setText(String.valueOf(snapBean.getEndId()));
	}
	
	private boolean checkUI(){
		if(checkTextField(this.beginIdText) == false){
			DialogUtils.showError(getShell(), "主机不能为空");
			return false;
		}
		
		if(checkTextField(this.endIdText) == false){
			DialogUtils.showError(getShell(), "端口不能为空");
			return false;
		}
		
		return true;
	}
	
	private boolean checkTextField(Text text){
		String value  = text.getText().trim();
		if(StringUtil.isBlank(value)){			
			return false;
		}
		return true;
	}
	
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			if(checkUI() == false){
				return;
			}
		}
		
		snapBean.setBeginId(Long.valueOf(beginIdText.getText().trim()));
		snapBean.setEndId(Long.valueOf(endIdText.getText().trim()));
		super.buttonPressed(buttonId);
	}
	
	public SnapBean getSnapBean() {
		return snapBean;
	}
}
