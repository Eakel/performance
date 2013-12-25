package com.easyfun.eclipse.performance.mysql.analyze.pref;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.rowset.JdbcRowSet;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.easyfun.eclipse.performance.mysql.MySQLActivator;

/**
 * MySQL JDBC …Ë÷√
 * 
 * @author linzhaoming
 *
 * 2013-12-23
 *
 */
public class MySQLJDBCPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public static final String PREF_ID = "com.easyfun.eclipse.performance.mysql.preferences.MySQLJDBC";
	
	private StringFieldEditor urlEditor = null;
	private StringFieldEditor driverEditor = null;
	private StringFieldEditor userEditor = null;
	private StringFieldEditor passwordEditor = null;

	public MySQLJDBCPreferencePage() {
		super(GRID);
		setPreferenceStore(MySQLActivator.getDefault().getPreferenceStore());
		setDescription("MySQL");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		urlEditor = new StringFieldEditor(MySQLPrefConstants.MYSQL_JDBC_URL, "&URL:", getFieldEditorParent());
		addField(urlEditor);
		driverEditor = new StringFieldEditor(MySQLPrefConstants.MYSQL_JDBC_DRIVER, "&Driver:", getFieldEditorParent());
		addField(driverEditor);
		userEditor = new StringFieldEditor(MySQLPrefConstants.MYSQL_JDBC_USER, "&User", getFieldEditorParent());
		addField(userEditor);
		passwordEditor = new StringFieldEditor(MySQLPrefConstants.MYSQL_JDBC_PASSWORD, "&Password", getFieldEditorParent());
		addField(passwordEditor);
	}
	

	protected void contributeButtons(Composite parent) {
		GridLayout layout = (GridLayout)parent.getLayout();
		layout.numColumns = layout.numColumns + 2;
		super.contributeButtons(parent);
		Button testButton = new Button(parent, SWT.PUSH);
		testButton.setLayoutData(new GridData());
		testButton.setText("≤‚  ‘");
		Dialog.applyDialogFont(testButton);
		testButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String url = urlEditor.getStringValue().trim();
				String name = userEditor.getStringValue().trim();
				String password = passwordEditor.getStringValue().trim();
				String driver = driverEditor.getStringValue().trim();
				try {
					Class.forName(driver);
					Connection conn = DriverManager.getConnection(url, name, password);
					conn.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	public void init(IWorkbench workbench) {
	}

}