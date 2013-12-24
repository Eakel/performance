package com.easyfun.eclipse.common.view.item.pub;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;

import com.easyfun.eclipse.common.config.cfg.Item;
import com.easyfun.eclipse.common.util.DialogUtils;
import com.easyfun.eclipse.common.view.item.welcome.WelcomeView;

/**
 * ��View ID
 * 
 * @author linzhaoming
 *
 * 2013-7-19
 *
 */
public class OpenViewItemHelper implements ItemHelper{

	public String getIcon(Item item) {
		return item.getIcon();
	}

	public void onDbClk(IWorkbenchPage page, Item item) {
		try {
			//TODO:֧�ֶ��
			page.showView(item.getViewId());
			
			//��Welcome���ӣ��ر�Welcome
			//TODO: ��BUG��
			IViewPart viewPart = page.findView(WelcomeView.VIEW_ID);
			boolean visible = page.isPartVisible(viewPart);
			if(visible){
				page.hideView(viewPart);
			}
		} catch (Exception e) {
			e.printStackTrace();
			DialogUtils.showError(page.getWorkbenchWindow().getShell(), "��View�쳣:" + e.getMessage());
		}
	}
	
}
