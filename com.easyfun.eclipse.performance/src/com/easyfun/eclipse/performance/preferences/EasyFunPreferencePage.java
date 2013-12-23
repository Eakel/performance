package com.easyfun.eclipse.performance.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import com.easyfun.eclipse.performance.PerformanceActivator;

/**
 * EasyFun设置
 * @author linzhaoming
 *
 * 2011-5-7
 *
 */
public class EasyFunPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public EasyFunPreferencePage() {
		super(GRID);
		setPreferenceStore(PerformanceActivator.getDefault().getPreferenceStore());
		setDescription("EasyFun自定义设置");
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