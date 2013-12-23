package com.easyfun.eclipse.performance.mysql.analyze.pref;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
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
public class MySQLPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public static final String ID = "com.easyfun.eclipse.performance.preferences.MySQL";

	public MySQLPreferencePage() {
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
		addField(new StringFieldEditor(MySQLPrefConstants.MYSQL_JDBC_URL, "&URL:", getFieldEditorParent()));
		addField(new StringFieldEditor(MySQLPrefConstants.MYSQL_JDBC_DRIVER, "&Driver:", getFieldEditorParent()));
		addField(new StringFieldEditor(MySQLPrefConstants.MYSQL_JDBC_USER, "&User", getFieldEditorParent()));
		addField(new StringFieldEditor(MySQLPrefConstants.MYSQL_JDBC_PASSWORD, "&Password", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}

}