package com.easyfun.eclipse.component.ftp;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog for FTP settting
 * 
 * @author linzhaoming
 *
 * 2011-4-2
 *
 */
public class FTPHostDialog extends TitleAreaDialog {

	private Text nameText;	//FTP命名
	
	private Text remotePathText;
	private Text passwordText;
	private Text usernameText;
	private Text portText;
	private Text hostText;
	
	private FTPHostBean ftpBean;
	private Combo ftpTypeCombo;
	
	private int type = 1;
	
	private static final String FTP_DEFAULT_PORT = "21";
	private static final String SFTP_DEFAULT_PORT = "22";
	
	/** 新增 */
	public static final int TYPE_ADD = 1;
	/** 修改 */
	public static final int TYPE_EDIT = 2;
	
	public FTPHostDialog(Shell parentShell, int type) {
		super(parentShell);
		this.type = type;
		this.ftpBean = new FTPHostBean();
	}
	
	public FTPHostDialog(Shell parentShell, int type, FTPHostBean ftpBean) {
		super(parentShell);
		this.type = type;
		this.ftpBean = ftpBean;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		
		Composite container = new Composite(composite, SWT.NULL);
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		container.setLayout(gridLayout);
		
		Label label = new Label(container, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("Name: ");
		
		nameText = new Text(container, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		nameText.setLayoutData(gridData);
		nameText.setText("");
		nameText.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				nameText.selectAll();
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
				if(ftpTypeCombo.getSelectionIndex() == FTPHostBean.TYPE_FTP){
					portText.setText(FTP_DEFAULT_PORT);
					portText.setFocus();
				}else if(ftpTypeCombo.getSelectionIndex() == FTPHostBean.TYPE_SFTP){
					portText.setText(SFTP_DEFAULT_PORT);
					portText.setFocus();
				}
			}
		});
		
		label = new Label(container, SWT.NONE);
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		label.setLayoutData(gridData);
		label.setText("主机：");

		hostText = new Text(container, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		hostText.setLayoutData(gridData);
		hostText.setText("");
		hostText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
			}
		});
		
		label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label.setText("端口：");

		portText = new Text(container, SWT.BORDER);
		portText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
			}
		});


		label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label.setText("用户名：");

		usernameText = new Text(container, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.horizontalSpan =3;
		usernameText.setLayoutData(gridData);
		usernameText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
			}
		});
		usernameText.setText("");

		label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label.setText("密码：");

		passwordText = new Text(container, SWT.PASSWORD);
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		gridData.horizontalSpan =3;
		passwordText.setLayoutData(gridData);
		passwordText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
			}
		});
		passwordText.setText("");
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.horizontalSpan =3;
		passwordText.setLayoutData(gridData);

		

		label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label.setText("FTP路径：");

		remotePathText = new Text(container, SWT.BORDER);
		remotePathText.setText("");
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.horizontalSpan =3;
		remotePathText.setLayoutData(gridData);
		
		initialize();
		
		return container;
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("FTP连接信息");
	}
	
	private void initialize(){
		if(type == TYPE_ADD){
			nameText.setText("newUrlName");
			ftpTypeCombo.setEnabled(true);
			ftpTypeCombo.select(0);
			portText.setText(FTP_DEFAULT_PORT);
			hostText.setText("");
			usernameText.setText("");
			passwordText.setText("");
			remotePathText.setText("");
			
			nameText.setFocus();
		}else if(type == TYPE_EDIT){
			nameText.setText(ftpBean.getName());
			ftpTypeCombo.setEnabled(false);
			if(ftpBean != null){
				portText.setText("" + ftpBean.getPort());				
				if(ftpBean.getFtpType() == FTPHostBean.TYPE_FTP){
					ftpTypeCombo.select(0);
				}else if(ftpBean.getFtpType() == FTPHostBean.TYPE_SFTP){
					ftpTypeCombo.select(1);
				}
				hostText.setText(ftpBean.getHost());
				usernameText.setText(ftpBean.getUsername());
				passwordText.setText(ftpBean.getPassword());
				remotePathText.setText(ftpBean.getRemotePath());
			}
			
			nameText.setFocus();
		}
	}
	
	private boolean checkUI(){
		if(checkTextField(this.hostText) == false){
			this.setErrorMessage("URL名字不能为空");
			this.hostText.setFocus();
			return false;
		}
		
		if(checkTextField(this.portText) == false){
			this.setErrorMessage("端口不能为空");
			this.portText.setFocus();
			return false;
		}
		
		if(checkTextField(this.usernameText) == false){
			this.setErrorMessage("用户名不能为空");
			this.usernameText.setFocus();
			return false;
		}
		
		if(checkTextField(this.passwordText) == false){
			this.setErrorMessage("密码不能为空");
			this.passwordText.setFocus();
			return false;
		}
		
		return true;
	}
	
	private boolean checkTextField(Text text){
		String value  = text.getText().trim();
		if(StringUtils.isBlank(value)){			
			return false;
		}
		return true;
	}
	
	protected void okPressed() {
		if (checkUI() == false) {
			return;
		}
		
		if(type == TYPE_ADD){
			if(ftpTypeCombo.getSelectionIndex() ==0){
				ftpBean = new FTPHostBean();
				ftpBean.setFtpType(FTPHostBean.TYPE_FTP);
			}else if(ftpTypeCombo.getSelectionIndex() == 1){
				ftpBean = new FTPHostBean();
				ftpBean.setFtpType(FTPHostBean.TYPE_SFTP);
			}
			
			ftpBean.setName(nameText.getText().trim());
			ftpBean.setHost(hostText.getText().trim());
			ftpBean.setPort(Integer.parseInt(portText.getText().trim()));
			ftpBean.setUsername(usernameText.getText().trim());
			ftpBean.setPassword(passwordText.getText().trim());
			ftpBean.setRemotePath(remotePathText.getText().trim());
		} else if (type == TYPE_EDIT) {
			ftpBean.setName(nameText.getText().trim());
			ftpBean.setHost(hostText.getText().trim());
			ftpBean.setPort(Integer.parseInt(portText.getText().trim()));
			ftpBean.setUsername(usernameText.getText().trim());
			ftpBean.setPassword(passwordText.getText().trim());
			ftpBean.setRemotePath(remotePathText.getText().trim());
		}
		
		super.okPressed();
	}
	
	public FTPHostBean getFTPBean() {
		return ftpBean;
	}
}
