package com.easyfun.eclipse.common.view.item.pub;

import org.eclipse.ui.IWorkbenchPage;

import com.easyfun.eclipse.common.config.cfg.Item;

/**
 * ����������Ҫʵ�ֵĽӿ�
 * @author linzhaoming
 *
 * 2011-5-8
 *
 */
public interface ItemHelper {
	/**
	 * 
	 * @param page Activepagae
	 * @param item �����Ķ���
	 */
	public void onDbClk(IWorkbenchPage page, Item item);
	
	public String getIcon(Item item); 
}
