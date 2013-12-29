package com.easyfun.eclipse.performance.navigator.cfg.model;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.navigator.helper.DefaultItemHelper;
import com.easyfun.eclipse.performance.navigator.helper.ItemHelper;
import com.easyfun.eclipse.performance.navigator.helper.OpenViewItemHelper;
import com.easyfun.eclipse.util.ClassUtil;

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
			throw new RuntimeException("��ʼ��ʧ��");
		}
	}

	public String getComposite() {
		return item.getComposite();
	}

	public ItemHelper getHelper() {
		return helper;
	}
	
}
