package com.easyfun.eclipse.common.navigator.helper;

import org.eclipse.swt.widgets.Composite;

import com.easyfun.eclipse.common.navigator.cfg.model.Item;

/**
 * ������View�е�Composite������ʵ����Ҫextends �����
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
