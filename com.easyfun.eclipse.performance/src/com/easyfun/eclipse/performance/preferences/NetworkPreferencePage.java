package com.easyfun.eclipse.performance.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import com.easyfun.eclipse.performance.PerformanceActivator;

/**
 * Õ¯¬Á…Ë÷√
 * @author linzhaoming
 *
 * 2011-5-7
 *
 */
public class NetworkPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public NetworkPreferencePage() {
		super(GRID);
		setPreferenceStore(PerformanceActivator.getDefault().getPreferenceStore());
		setDescription("Õ¯¬Á…Ë÷√");
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