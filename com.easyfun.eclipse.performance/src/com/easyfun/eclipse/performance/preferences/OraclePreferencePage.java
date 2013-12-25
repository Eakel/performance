package com.easyfun.eclipse.performance.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.easyfun.eclipse.performance.PerformanceActivator;

/**
 * EasyFun设置
 * @author linzhaoming
 *
 * 2011-5-7
 *
 */
public class OraclePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public OraclePreferencePage() {
		super(GRID);
		setPreferenceStore(PerformanceActivator.getDefault().getPreferenceStore());
		setDescription("设置Oracle相关的自定义配置");
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