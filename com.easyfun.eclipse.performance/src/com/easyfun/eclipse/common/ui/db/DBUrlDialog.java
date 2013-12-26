package com.easyfun.eclipse.common.ui.db;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 数据库URL设置窗口
 * 
 * @author linzhaoming
 *
 * 2013-12-26
 *
 */
public class DBUrlDialog extends TitleAreaDialog {
	private Text nameText;	//URL命名
	private Text urlText;
	private Text driverClassText;
	private Text usernameText;
	private Text passwordText;
	
	private static final String ORACLE_DEFAULT_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	
	private static final String MYSQL_DEFAULT_URL = "jdbc:mysql://127.0.0.1:3306/information_schema";
	
	private static final String ORACLE_DEFAUT_DRIVER = "oracle.jdbc.driver.OracleDriver";
	
	private static final String MYSQL_DEFAUT_DRIVER = "com.mysql.jdbc.Driver";
	
	/** 新增 */
	public static final int TYPE_ADD = 1;
	/** 修改 */
	public static final int TYPE_EDIT = 2;
	
	private int type = 1;
	private Combo dbTypeCombo;
	
	private DBUrlBean urlBean;

	public DBUrlDialog(Shell shell, int type, DBUrlBean jdbcUrlBean) {
		super(shell);
		this.type = type;
		this.urlBean = jdbcUrlBean;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		
		createSetttingArea(composite);
		
		setTitle("数据库URL设置");		
		
		initial();
		
		return parent;
	}
	

	protected void okPressed() {
		if(checkEmpty() == false){
			return;
		}else{
			if(type == TYPE_ADD){
				if(dbTypeCombo.getSelectionIndex() ==0){
					urlBean = new DBUrlBean(DBTypeEnum.Oracle);
				}else if(dbTypeCombo.getSelectionIndex() == 1){
					urlBean = new DBUrlBean(DBTypeEnum.MySQL);
				}
				
				urlBean.setDriverClass(driverClassText.getText().trim());
				urlBean.setUrl(urlText.getText().trim());
				urlBean.setUsername(usernameText.getText().trim());
				urlBean.setPassword(passwordText.getText().trim());
			} else if (type == TYPE_EDIT) {
				urlBean.setUrl(urlText.getText().trim());
				urlBean.setUsername(usernameText.getText().trim());
				urlBean.setPassword(passwordText.getText().trim());
			}
			super.okPressed();
		}
	}
	
	private void createSetttingArea(Composite parent){
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(2, false));
		
		Label label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("Name: ");
		
		nameText = new Text(composite, SWT.BORDER);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.setText("");
		nameText.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e) {
				nameText.selectAll();
			}
		});
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("Type: ");
		
		dbTypeCombo = new Combo(composite, SWT.READ_ONLY);
		dbTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dbTypeCombo.setItems(new String[]{"Oracle", "MySQL"});
		dbTypeCombo.select(0);
		dbTypeCombo.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				int select = dbTypeCombo.getSelectionIndex();
				if(select ==0 ){
					urlText.setText(ORACLE_DEFAULT_URL);	//Oracle
					driverClassText.setText(ORACLE_DEFAUT_DRIVER);
					urlText.setFocus();
				}else if(select ==1){
					urlText.setText(MYSQL_DEFAULT_URL);	//MySQL
					driverClassText.setText(MYSQL_DEFAUT_DRIVER);
					urlText.setFocus();
				}
			}
		});
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("URL: ");
		
		urlText = new Text(composite, SWT.BORDER);
		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		urlText.setText("");
		urlText.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e) {
				urlText.selectAll();
			}
		});
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("Driver: ");
		
		driverClassText = new Text(composite, SWT.BORDER|SWT.READ_ONLY);
		driverClassText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		driverClassText.setText("");
		
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("User: ");
		
		usernameText = new Text(composite, SWT.BORDER);
		usernameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		usernameText.setText("");
		usernameText.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e) {
				usernameText.selectAll();
			}
		});
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("Password: ");
		
		passwordText = new Text(composite, SWT.PASSWORD | SWT.BORDER);
		passwordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		passwordText.setText("");
		passwordText.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e) {
				passwordText.selectAll();
			}
		});
		
		init();
	}
	
	private void init(){
		if(type == TYPE_ADD){
			nameText.setText("newUrlName");
			dbTypeCombo.setEnabled(true);
			dbTypeCombo.select(0);
			urlText.setText(ORACLE_DEFAULT_URL);
			driverClassText.setText(ORACLE_DEFAUT_DRIVER);
			usernameText.setText("");
			passwordText.setText("");
			
			nameText.setFocus();
		}else if(type == TYPE_EDIT){
			nameText.setText(urlBean.getName());
			dbTypeCombo.setEnabled(false);
			if(urlBean != null){
				driverClassText.setText(urlBean.getDriverClass());				
				if(urlBean.getDBType() == DBTypeEnum.Oracle){
					dbTypeCombo.select(0);
				}else if(urlBean.getDBType() == DBTypeEnum.MySQL){
					dbTypeCombo.select(1);
				}
				urlText.setText(urlBean.getUrl());
				usernameText.setText(urlBean.getUsername());
				passwordText.setText(urlBean.getPassword());
			}
			
			nameText.setFocus();
		}
	}
	
	private boolean checkEmpty(){
		String name = nameText.getText().trim();
		String username = usernameText.getText().trim();
		String driverClass = driverClassText.getText().trim();
		String password = passwordText.getText().trim();
		String url = urlText.getText().trim();
		
		if(name.equals("")){
			this.setErrorMessage("URL名字不能为空");
			nameText.setFocus();
			return false;
		}
		
		if(url.equals("")){
			this.setErrorMessage("URL不能为空");
			urlText.setFocus();
			return false;
		}
		
		if(driverClass.equals("")){
			this.setErrorMessage("驱动类不能为空");
			driverClassText.setFocus();
			return false;
		}
		
		if(username.equals("")){
			this.setErrorMessage("用户名不能为空");
			usernameText.setFocus();
			return false;
		}
		
		if(password.equals("")){
			this.setErrorMessage("密码不能为空");
			passwordText.setFocus();
			return false;
		}

		return true;
	}
	
	public DBUrlBean getCreateBean(){
		return urlBean;
	}

	/**
	 * 初始化对话框数据
	 */
	private void initial(){
//		ConnectionModel model = ConnectionFactory.getConnectionModel();
//		nameText.setText(model.getName());
//		driverText.setText(model.getDriver());
//		passText.setText(model.getPassword());
//		urlText.setText(model.getUrl());
	}
}
