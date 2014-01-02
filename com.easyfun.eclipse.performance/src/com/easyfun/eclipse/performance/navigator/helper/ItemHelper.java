package com.easyfun.eclipse.performance.navigator.helper;

import org.eclipse.ui.IWorkbenchPage;

import com.easyfun.eclipse.performance.navigator.cfg.model.Item;
import com.easyfun.eclipse.performance.navigator.cfg.model.Node;

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
	
	public String getIcon(Node node); 
}
