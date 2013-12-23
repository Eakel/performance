package com.easyfun.eclipse.performance;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import com.easyfun.eclipse.common.UIConstants;
import com.easyfun.eclipse.common.config.cfg.Folder;
import com.easyfun.eclipse.common.config.cfg.Item;
import com.easyfun.eclipse.common.view.item.pub.DefaultItemProvider;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(UIConstants.VIEWID_NAVIGATION,  false, IPageLayout.LEFT, 0.15f, editorArea);
		layout.getViewLayout(UIConstants.VIEWID_NAVIGATION).setCloseable(false);
		IFolderLayout topFolder = layout.createFolder("top", IPageLayout.TOP, 0.5f, editorArea);
		topFolder.addPlaceholder(UIConstants.VIEWID_MAINCONTENT + ":*");
		topFolder.addView(UIConstants.VIEWID_MAINCONTENT);
		
		//处理所有扩展的ViewId，统一显示在同一个地方
		List<Folder> folders = DefaultItemProvider.getNavigator().getFolders();
		for (Folder folder : folders) {
			if (("true").equalsIgnoreCase(folder.getVisible())) {
				List<Item> items = folder.getItems();
				for (Item item : items) {
					if (("true").equalsIgnoreCase(item.getVisible())) {
						String viewId = item.getViewId();
						if(StringUtils.isNotEmpty(viewId)){
							topFolder.addPlaceholder(viewId);
						}
					}
				}
			}
		}
		
		//Console
		IFolderLayout bottomFolder = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.80f, "top");
		bottomFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		layout.getViewLayout(IConsoleConstants.ID_CONSOLE_VIEW).setCloseable(true);
	}
}
