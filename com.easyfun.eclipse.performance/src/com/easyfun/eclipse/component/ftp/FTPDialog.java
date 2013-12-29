package com.easyfun.eclipse.component.ftp;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.rcp.RCPUtil;

/**
 * Dialog for FTP settting
 * 
 * @author linzhaoming
 *
 * 2011-4-2
 *
 */
public class FTPDialog extends Dialog {

	private Text filePathText;
	private Text passwdText;
	private Text userNameText;
	private Text portText;
	private Text hostText;
	
	private FTPBean ftpBean;
	private Combo ftpTypeCombo;
	
	public FTPDialog(Shell parentShell, FTPBean ftpBean) {
		super(parentShell);
		this.ftpBean = ftpBean;
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		container.setLayout(gridLayout);
		Label label = new Label(container, SWT.NONE);
		GridData gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		label.setLayoutData(gridData);
		label.setText("主机：");

		hostText = new Text(container, SWT.BORDER);
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		hostText.setLayoutData(gridData);
		hostText.setText("localhost");
		hostText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
//				updateUI();
			}
		});
		
		label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label.setText("端口：");

		portText = new Text(container, SWT.BORDER);
		portText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
//				updateUI();
			}
		});
		
		label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label.setText("服务器类型：");

		ftpTypeCombo = new Combo(container, SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.horizontalSpan =3;
		ftpTypeCombo.setLayoutData(gridData);
		ftpTypeCombo.add("FTP - File Transfer Protocol");
		ftpTypeCombo.add("SFTP - SSH File Transfer Protocol");
		ftpTypeCombo.select(0);
		ftpTypeCombo.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(ftpTypeCombo.getSelectionIndex() == FTPBean.TYPE_FTP){
					portText.setText("21");
					portText.setFocus();
				}else if(ftpTypeCombo.getSelectionIndex() == FTPBean.TYPE_SFTP){
					portText.setText("22");
					portText.setFocus();
				}
			}
		});

		label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label.setText("用户名：");

		userNameText = new Text(container, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.horizontalSpan =3;
		userNameText.setLayoutData(gridData);
		userNameText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
//				updateUI();
			}
		});
		userNameText.setText("linzm");

		label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label.setText("密码：");

		passwdText = new Text(container, SWT.PASSWORD);
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		gridData.horizontalSpan =3;
		passwdText.setLayoutData(gridData);
		passwdText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
//				updateUI();
			}
		});
		passwdText.setText("aaa");
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.horizontalSpan =3;
		passwdText.setLayoutData(gridData);

		hostText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label.setText("文件路径：");

		filePathText = new Text(container, SWT.BORDER);
		filePathText.setText("jvm.log");
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.horizontalSpan =3;
		filePathText.setLayoutData(gridData);
		
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
		newShell.setText("FTP连接信息");
	}
	
	private void initialize(){
		userNameText.setText(ftpBean.getUserName());
		passwdText.setText(ftpBean.getPasswd());
		hostText.setText(ftpBean.getHost());
		filePathText.setText(ftpBean.getFilePath());
		portText.setText(String.valueOf(ftpBean.getPort()));
		ftpTypeCombo.select(ftpBean.getFtpType());
	}
	
	private boolean checkUI(){
		if(checkTextField(this.hostText) == false){
			RCPUtil.showError(getShell(), "主机不能为空");
			return false;
		}
		
		if(checkTextField(this.portText) == false){
			RCPUtil.showError(getShell(), "端口不能为空");
			return false;
		}
		
		if(checkTextField(this.userNameText) == false){
			RCPUtil.showError(getShell(), "用户名不能为空");
			return false;
		}
		
		if(checkTextField(this.passwdText) == false){
			RCPUtil.showError(getShell(), "密码不能为空");
			return false;
		}
		
//		if(checkTextField(this.filePathText) == false){
//			SWTUtil.showError(getShell(), "文件名不能为空");
//			return false;
//		}
		
		return true;
	}
	
	private boolean checkTextField(Text text){
		String value  = text.getText().trim();
		if(StringUtils.isBlank(value)){			
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
		ftpBean.setHost(hostText.getText().trim());
		ftpBean.setPort(Integer.parseInt(portText.getText().trim()));
		ftpBean.setUserName(userNameText.getText().trim());
		ftpBean.setPasswd(passwdText.getText().trim());
		ftpBean.setFilePath(filePathText.getText().trim());
		ftpBean.setFtpType(ftpTypeCombo.getSelectionIndex());
		super.buttonPressed(buttonId);
	}
	
	public FTPBean getFTPBean() {
		return ftpBean;
	}
}
