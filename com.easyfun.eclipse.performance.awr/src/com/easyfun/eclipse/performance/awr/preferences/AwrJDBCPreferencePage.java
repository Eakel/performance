package com.easyfun.eclipse.performance.awr.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.easyfun.eclipse.performance.awr.AwrActivator;

/**
 * Oracle JDBC …Ë÷√
 * @author linzhaoming
 *
 * 2011-5-7
 *
 */
public class AwrJDBCPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public static final String PREF_ID = "com.easyfun.eclipse.performance.awr.preferencePages.AwrJDBC";

	public AwrJDBCPreferencePage() {
		super(GRID);
		setPreferenceStore(AwrActivator.getDefault().getPreferenceStore());
		setDescription("Oracle");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(new StringFieldEditor(AwrPrefConstants.AWR_JDBC_URL, "&URL:", getFieldEditorParent()));
		addField(new StringFieldEditor(AwrPrefConstants.AWR_JDBC_DRIVER, "&Driver:", getFieldEditorParent()));
		addField(new StringFieldEditor(AwrPrefConstants.AWR_JDBC_USER, "&User", getFieldEditorParent()));
		addField(new StringFieldEditor(AwrPrefConstants.AWR_JDBC_PASSWORD, "&Password", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}

}