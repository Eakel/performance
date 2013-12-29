package ftp;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.component.ftp.FTPBean;

/**
 * 连接设定Diaglog
 * 
 * @author linzhaoming
 *
 * 2013-12-27
 *
 */
public class FTPConnectionDialog extends Dialog {
	private static final String DIALOG_SETTING_FILE = "ftp.connection.xml";
	
	private static final String KEY_TYPE = "TYPE";

	private static final String KEY_HOST = "HOST";

	private static final String KEY_PORT = "PORT";

	private static final String KEY_USERNAME = "USER";

	private static final String KEY_PASSWORD = "PASSWORD";

	private Text hostText;

	private Text portText;

	private Text usernameText;

	private Text passwordtext;

	private DialogSettings dialogSettings;

	private FTPBean ftpBean;
	
	private Combo ftpTypeCombo;

	public FTPConnectionDialog(FTPWindow window) {
		super(window.getShell());
		ftpBean = null;

		dialogSettings = new DialogSettings("FTP");
		try {
			dialogSettings.load(DIALOG_SETTING_FILE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("设置连接");

		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label.setText("服务器类型：");

		ftpTypeCombo = new Combo(composite, SWT.READ_ONLY);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
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

		label = new Label(composite, SWT.NULL);
		label.setText("主机：");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		
		hostText = new Text(composite, SWT.BORDER);
		hostText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		label = new Label(composite, SWT.NULL);
		label.setText("端口：");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		portText = new Text(composite, SWT.BORDER);
		portText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		label = new Label(composite, SWT.NULL);
		label.setText("用户名：");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		usernameText = new Text(composite, SWT.BORDER);
		usernameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		label = new Label(composite, SWT.NULL);
		label.setText("密码：");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		passwordtext = new Text(composite, SWT.PASSWORD | SWT.BORDER);
		passwordtext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		try {
			hostText.setText(dialogSettings.get(KEY_HOST));
			portText.setText(dialogSettings.getInt(KEY_PORT) + "");
			usernameText.setText(dialogSettings.get(KEY_USERNAME));
			passwordtext.setText(dialogSettings.get(KEY_PASSWORD));
		} catch (Exception e) {
			// ignore.
		}

		return composite;
	}

	public FTPBean getFTPBean() {
		return ftpBean;
	}
	
	protected void okPressed() {
		try {
			if (!new File(DIALOG_SETTING_FILE).exists()) {
				new File(DIALOG_SETTING_FILE).createNewFile();
			}
			dialogSettings.put(KEY_HOST, hostText.getText().trim());
			dialogSettings.put(KEY_PORT, Integer.parseInt(portText.getText().trim()));
			dialogSettings.put(KEY_USERNAME, usernameText.getText().trim());
			dialogSettings.put(KEY_PASSWORD, passwordtext.getText().trim());
			dialogSettings.save(DIALOG_SETTING_FILE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ftpBean = new FTPBean();
		ftpBean.setHost(hostText.getText().trim());
		ftpBean.setPort(Integer.parseInt(portText.getText().trim()));
		ftpBean.setUserName(usernameText.getText().trim());
		ftpBean.setPasswd(passwordtext.getText().trim());

		super.okPressed();
	}

}