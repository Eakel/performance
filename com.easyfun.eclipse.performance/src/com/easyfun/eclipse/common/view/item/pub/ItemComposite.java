package com.easyfun.eclipse.common.view.item.pub;

import org.eclipse.swt.widgets.Composite;

import com.easyfun.eclipse.common.config.cfg.Item;

/**
 * 放置在View中的Composite，具体实现需要extends 这个类
 * 
 * @author linzhaoming
 * 
 *         2011-5-8
 * 
 */
public class ItemComposite extends Composite {
	protected Item item;

	public ItemComposite(Composite parent, int style, Item item) {
		super(parent, style);
		this.item = item;
	}
}
