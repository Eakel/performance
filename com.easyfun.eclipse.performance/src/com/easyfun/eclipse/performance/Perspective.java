package com.easyfun.eclipse.performance;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import com.easyfun.eclipse.performance.helper.IConstants;
import com.easyfun.eclipse.performance.navigator.ItemNavigationView;
import com.easyfun.eclipse.performance.navigator.WelcomeView;
import com.easyfun.eclipse.performance.navigator.cfg.model.Item;
import com.easyfun.eclipse.performance.navigator.helper.DefaultItemProvider;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(ItemNavigationView.VIEW_ID,  false, IPageLayout.LEFT, 0.15f, editorArea);
		layout.getViewLayout(ItemNavigationView.VIEW_ID).setCloseable(false);
		IFolderLayout topFolder = layout.createFolder("top", IPageLayout.TOP, 0.5f, editorArea);
//		topFolder.addPlaceholder(MainContentView.VIEW_ID + ":*");
//		topFolder.addView(MainContentView.VIEW_ID);
		topFolder.addPlaceholder(WelcomeView.VIEW_ID + ":*");
		topFolder.addView(WelcomeView.VIEW_ID);
		
		//处理所有扩展的ViewId，统一显示在同一个地方
		List<Item> items = DefaultItemProvider.getNavigator().getAllItems();
		for (Item item : items) {
			if (item.isVisible()) {
				String viewId = item.getViewId();
				if (StringUtils.isNotEmpty(viewId)) {
					topFolder.addPlaceholder(viewId);
				}
			}
		}
		
		//Console
		IFolderLayout bottomFolder = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.80f, "top");
		bottomFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		layout.getViewLayout(IConsoleConstants.ID_CONSOLE_VIEW).setCloseable(true);
		
		bottomFolder.addPlaceholder(IConstants.ERROR_LOG_VIEW_ID);
		
//		layout.setFixed(true);	//TODO:校验是否可以固定Perspective
	}
}
