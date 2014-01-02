package com.easyfun.eclipse.performance.appframe.webtool.bo.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * 
 * @author zhaoming
 * 
 *         Jan 3, 2008
 */
public class BOCommentEditor extends FormEditor {
	public static String EDITOR_ID = "com.easyfun.littletools.bo.editors.bocommentEditor";
	public static int PROP_TASK = 200;
	private IEditorInput editorInput = null;

	protected void addPages() {
		try {
			TextEditor textEditor = new TextEditor();
			addPage(textEditor, editorInput);
			setPageText(0, "Table Comment");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	public void doSave(IProgressMonitor monitor) {
		firePropertyChange(PROP_TASK);
	}

	public void doSaveAs() {
//		page.doSaveAs();
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		this.editorInput = input;
	}

}
