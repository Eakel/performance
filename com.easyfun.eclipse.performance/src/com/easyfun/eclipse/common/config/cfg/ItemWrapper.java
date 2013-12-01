package com.easyfun.eclipse.common.config.cfg;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.common.util.ClassUtil;
import com.easyfun.eclipse.common.view.item.pub.DefaultItemHelper;
import com.easyfun.eclipse.common.view.item.pub.ItemHelper;
import com.easyfun.eclipse.common.view.item.pub.OpenViewItemHelper;

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
			Class helperClass = null;
			if(StringUtils.isEmpty(item.getHelper())){
				helperClass = DefaultItemHelper.class;
			} else if(StringUtils.equals(OpenViewItemHelper.class.getName(), item.getHelper())){
				helperClass = OpenViewItemHelper.class;
			} else{
				helperClass = ClassUtil.loadBundleClass(item.getPluginId(), item.getHelper());	
			}
			
			helper = (ItemHelper)helperClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("≥ı ºªØ ß∞‹");
		}
	}

	public String getComposite() {
		return item.getComposite();
	}

	public ItemHelper getHelper() {
		return helper;
	}
	
}
