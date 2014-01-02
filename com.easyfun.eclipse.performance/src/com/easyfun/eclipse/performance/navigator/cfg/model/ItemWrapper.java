package com.easyfun.eclipse.performance.navigator.cfg.model;

import com.easyfun.eclipse.performance.navigator.helper.ItemHelper;
import com.easyfun.eclipse.performance.navigator.helper.OpenViewItemHelper;

public class ItemWrapper {
	private Item item;
	
	private ItemHelper helper;
	
	public ItemWrapper(Item item){
		setItem(item);
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
		try {
			Class<?> helperClass = OpenViewItemHelper.class;
			helper = (ItemHelper)helperClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("≥ı ºªØ ß∞‹");
		}
	}

	public ItemHelper getHelper() {
		return helper;
	}
	
}
