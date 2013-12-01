package com.easyfun.eclipse.performance;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import com.easyfun.eclipse.common.UIConstants;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(UIConstants.VIEWID_NAVIGATION,  false, IPageLayout.LEFT, 0.15f, editorArea);
		layout.getViewLayout(UIConstants.VIEWID_NAVIGATION).setCloseable(false);
		IFolderLayout topFolder = layout.createFolder("top", IPageLayout.TOP, 0.5f, editorArea);
		topFolder.addPlaceholder(UIConstants.VIEWID_MAINCONTENT + ":*");
		topFolder.addView(UIConstants.VIEWID_MAINCONTENT);
		
		//Console
		IFolderLayout bottomFolder = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.80f, "top");
		bottomFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		layout.getViewLayout(IConsoleConstants.ID_CONSOLE_VIEW).setCloseable(true);
	}
}
