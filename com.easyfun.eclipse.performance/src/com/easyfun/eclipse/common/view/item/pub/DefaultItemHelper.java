package com.easyfun.eclipse.common.view.item.pub;

import org.eclipse.ui.IWorkbenchPage;

import com.easyfun.eclipse.common.UIConstants;
import com.easyfun.eclipse.common.config.cfg.Item;
import com.easyfun.eclipse.common.config.cfg.ItemWrapper;
import com.easyfun.eclipse.common.util.DialogUtils;
import com.easyfun.eclipse.common.view.item.content.MainContentView;

/**
 * 打开主内容View
 * <li>需要继承ItemComposite
 * 
 * @author linzhaoming
 *
 * 2013-7-19
 *
 */
public class DefaultItemHelper implements ItemHelper{
	public void onDbClk(IWorkbenchPage page, Item item) {
		try {
			ItemWrapper pair = DefaultItemProvider.getNavigatorByType(item);
			MainContentView mainContentView = (MainContentView)page.showView(UIConstants.VIEWID_MAINCONTENT);
			mainContentView.setCompositeName(pair);
		} catch (Exception e) {
			e.printStackTrace();
			DialogUtils.showError(page.getWorkbenchWindow().getShell(), "打开View异常:" + e.getMessage());
		}
	}
	
	public String getIcon(Item item){
		return item.getIcon();
	}
}
