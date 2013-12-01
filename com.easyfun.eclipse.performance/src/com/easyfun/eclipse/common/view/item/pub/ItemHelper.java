package com.easyfun.eclipse.common.view.item.pub;

import org.eclipse.ui.IWorkbenchPage;

import com.easyfun.eclipse.common.config.cfg.Item;

/**
 * 导航助手需要实现的接口
 * @author linzhaoming
 *
 * 2011-5-8
 *
 */
public interface ItemHelper {
	/**
	 * 
	 * @param page Activepagae
	 * @param item 导航的对象
	 */
	public void onDbClk(IWorkbenchPage page, Item item);
	
	public String getIcon(Item item); 
}
