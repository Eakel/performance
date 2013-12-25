package com.easyfun.eclipse.performance.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.easyfun.eclipse.performance.PerformanceActivator;

/**
 * EasyFun����
 * @author linzhaoming
 *
 * 2011-5-7
 *
 */
public class OraclePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public OraclePreferencePage() {
		super(GRID);
		setPreferenceStore(PerformanceActivator.getDefault().getPreferenceStore());
		setDescription("����Oracle��ص��Զ�������");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
	}

	public void init(IWorkbench workbench) {
	}

}