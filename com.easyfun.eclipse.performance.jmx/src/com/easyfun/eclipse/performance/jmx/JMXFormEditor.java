package com.easyfun.eclipse.performance.jmx;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * 
 * @author zhaoming
 * 
 *         Jan 3, 2008
 */
public class JMXFormEditor extends FormEditor {
	public static String EDITOR_ID = "com.easyfun.eclipse.jboss.editors.jmx";
	public static int PROP_TASK = 200;

	private JMXFormEditorInput editorInput = null;
	
	private JMXInfoFormPage infoPage;

	protected void addPages() {
		infoPage = new JMXInfoFormPage(this, "infoPage", "Info", editorInput.getMbeanModel());
	
		refreshEditor();
		
		try {
			addPage(infoPage);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		setTitleImage(JmxActivator.getImageDescriptor(JMXImageConstants.ICON_TASK_PATH).createImage());
	}

	public void doSave(IProgressMonitor monitor) {
		infoPage.doSave(monitor);
		firePropertyChange(PROP_TASK);
		refreshEditor();
	}

	public void doSaveAs() {
		infoPage.doSaveAs();
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		this.editorInput = (JMXFormEditorInput) input;
	}

	private void refreshEditor() {
		setPartName(editorInput.getName());
	}

}
