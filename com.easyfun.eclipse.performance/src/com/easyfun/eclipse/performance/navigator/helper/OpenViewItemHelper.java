package com.easyfun.eclipse.performance.navigator.helper;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;

import com.easyfun.eclipse.performance.navigator.WelcomeView;
import com.easyfun.eclipse.performance.navigator.cfg.model.Item;
import com.easyfun.eclipse.uiutil.RCPUtil;

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
			//��Welcome���ӣ��ر�Welcome
			IViewPart viewPart = page.findView(WelcomeView.VIEW_ID);
			boolean visible = page.isPartVisible(viewPart);
			if(visible){
				page.hideView(viewPart);
			}
			
			//TODO:֧�ֶ��
			page.showView(item.getViewId());
			
		} catch (Exception e) {
			e.printStackTrace();
			RCPUtil.showError(page.getWorkbenchWindow().getShell(), "��View�쳣:" + e.getMessage());
		}
	}
	
}
