package com.easyfun.eclipse.common.navigator.helper;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.easyfun.eclipse.common.navigator.cfg.model.Item;
import com.easyfun.eclipse.common.navigator.cfg.model.ItemWrapper;
import com.easyfun.eclipse.common.navigator.ui.MainContentView;
import com.easyfun.eclipse.common.util.DialogUtils;
import com.easyfun.eclipse.performance.PerformanceActivator;

/**
 * ��������View
 * <li>��Ҫ�̳�ItemComposite
 * 
 * @author linzhaoming
 *
 * 2013-7-19
 *
 */
public class DefaultItemHelper implements ItemHelper{
	public void onDbClk(IWorkbenchPage page, Item item) {
		try {
			ItemWrapper pair = DefaultItemProvider.getNavigatorByType(item);
			MainContentView mainContentView = (MainContentView)page.showView(MainContentView.VIEW_ID);
			
			//��̬������ʾ��ͼ��
			Image image = null;
			Item selItem = pair.getItem();
			ItemWrapper itemWrapper = DefaultItemProvider.getNavigatorByType(selItem);
			String iconPath = itemWrapper.getHelper().getIcon(selItem);
			
			String regKey = selItem.getPluginId() + "_" + iconPath;
			if (itemWrapper.getHelper().getIcon(selItem) != null) {
				ImageRegistry registry = PerformanceActivator.getDefault().getImageRegistry();
				if (registry.getDescriptor(regKey) == null) {
					ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(selItem.getPluginId(), iconPath);
					registry.put(regKey, descriptor);
				}
				image = registry.get(regKey);
			}
			
			mainContentView.setImage(image);
			mainContentView.setCompositeName(pair);
		} catch (Exception e) {
			e.printStackTrace();
			DialogUtils.showError(page.getWorkbenchWindow().getShell(), "��View�쳣:" + e.getMessage());
		}
	}
	
	public String getIcon(Item item){
		return item.getIcon();
	}
}
