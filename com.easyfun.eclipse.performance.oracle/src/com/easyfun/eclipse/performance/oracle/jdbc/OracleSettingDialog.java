//package com.easyfun.eclipse.performance.oracle.jdbc;
//
//import org.eclipse.jface.dialogs.TitleAreaDialog;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.FocusAdapter;
//import org.eclipse.swt.events.FocusEvent;
//import org.eclipse.swt.events.ModifyEvent;
//import org.eclipse.swt.events.ModifyListener;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Text;
//
//import com.easyfun.eclipse.common.util.DialogUtils;
//
///**
// * 数据库设置窗口
// * 
// * @author linzhaoming
// * Create Date: 2010-8-14
// */
//public class OracleSettingDialog extends TitleAreaDialog {
//	private Text urlText;
//	private Text driverText;
//	private Text nameText;
//	private Text passText;
//	
//	private ConnectionModel model;
//
//	public OracleSettingDialog(Shell shell, ConnectionModel model) {
//		super(shell);
//		this.model = model;
//	}
//
//	protected Control createDialogArea(Composite parent) {
//		Composite composite = (Composite)super.createDialogArea(parent);
//		createSetttingArea(composite);
//		setTitle("Oracle数据库设置");		
//		initial();
//		nameText.setFocus();
//		return parent;
//	}
//
//	protected void okPressed() {
//		if (checkInput() == true) {
////			OracleUtil.refreshConnectionModel(this.driverText.getText(),
////					this.nameText.getText().trim(), this.passText.getText()
////							.trim(), this.urlText.getText().trim());
//			super.okPressed();
//		}
//	}
//	
//	private void createSetttingArea(Composite parent){
//		Composite composite = new Composite(parent, SWT.NULL);
//		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		composite.setLayout(new GridLayout(2, false));
//		
//		Label label = new Label(composite, SWT.NULL);
//		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
//		label.setText("url: ");
//		
//		urlText = new Text(composite, SWT.BORDER);
//		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		urlText.setText("jdbc:oracle:thin:@127.0.0.1:1521:orcl");
//		urlText.addFocusListener(new FocusAdapter(){
//			public void focusGained(FocusEvent e) {
//				urlText.selectAll();
//			}
//		});
//		urlText.addModifyListener(new ModifyListener(){
//			public void modifyText(ModifyEvent e) {
//				model.setUrl(urlText.getText().trim());
//			}
//		});
//		
//		label = new Label(composite, SWT.NULL);
//		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
//		label.setText("Driver: ");
//		
//		driverText = new Text(composite, SWT.BORDER|SWT.READ_ONLY);	//TODO：目前不给改变
//		driverText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		driverText.setText("oracle.jdbc.driver.OracleDriver");
//		driverText.addFocusListener(new FocusAdapter(){
//			public void focusGained(FocusEvent e) {
//				driverText.selectAll();
//			}
//		});
//		
//		
//		label = new Label(composite, SWT.NULL);
//		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
//		label.setText("User: ");
//		
//		nameText = new Text(composite, SWT.BORDER);
//		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		nameText.setText("scott");
//		nameText.addFocusListener(new FocusAdapter(){
//			public void focusGained(FocusEvent e) {
//				nameText.selectAll();
//			}
//		});
//		nameText.addModifyListener(new ModifyListener(){
//			public void modifyText(ModifyEvent e) {
//				model.setName(nameText.getText().trim());
//			}
//		});
//		
//		label = new Label(composite, SWT.NULL);
//		label.setLayoutData(new GridData());
//		label.setText("Password: ");
//		
//		passText = new Text(composite, SWT.BORDER);
//		passText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		passText.setText("tiger");
//		passText.addFocusListener(new FocusAdapter(){
//			public void focusGained(FocusEvent e) {
//				passText.selectAll();
//			}
//		});
//		passText.addModifyListener(new ModifyListener(){
//			public void modifyText(ModifyEvent e) {
//				model.setPassword(passText.getText().trim());
//			}
//		});
//		
//	}
//
//	private boolean checkInput(){
//		if(checkEmpty() == false){
//			return false;
//		}
//		
////		if(checkTheSame() == false){
////			return false;
////		}
//		
//		return true;
//	}
//	
//	private boolean checkEmpty(){
//		String name = nameText.getText().trim();
//		String driver = driverText.getText().trim();
//		String password = passText.getText().trim();
//		String url = urlText.getText().trim();
//		
//		if(name.equals("")){
//			DialogUtils.showMsg(getShell(), "用户名不能为空");
//			return false;
//		}
//		
//		if(driver.equals("")){
//			DialogUtils.showMsg(getShell(), "驱动程序不能为空");
//			return false;
//		}
//		
//		if(password.equals("")){
//			DialogUtils.showMsg(getShell(), "密码不能为空");
//			return false;
//		}
//		
//		if(url.equals("")){
//			DialogUtils.showMsg(getShell(), "URL不能为空");
//			return false;
//		}
//		return true;
//	}
//	
//	private boolean checkTheSame(){
//		String name = nameText.getText().trim();
//		String driver = driverText.getText().trim();
//		String password = passText.getText().trim();
//		String url = urlText.getText().trim();
//		return true;
//	}
//	
//	/**
//	 * 初始化对话框数据
//	 */
//	private void initial(){
//		if(model == null){
//			model = new ConnectionModel();
//		}
//		nameText.setText(model.getName());
//		driverText.setText(model.getDriver());
//		passText.setText(model.getPassword());
//		urlText.setText(model.getUrl());
//	}
//	
//	public ConnectionModel getConnectionModel(){
//		return model;
//	}
//}
