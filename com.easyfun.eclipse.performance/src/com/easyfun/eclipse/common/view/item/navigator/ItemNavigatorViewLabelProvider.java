package com.easyfun.eclipse.common.view.item.navigator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.easyfun.eclipse.common.config.cfg.Folder;
import com.easyfun.eclipse.common.config.cfg.Item;
import com.easyfun.eclipse.common.config.cfg.ItemWrapper;
import com.easyfun.eclipse.common.view.item.pub.DefaultItemProvider;
import com.easyfun.eclipse.performance.PerformanceActivator;

public class ItemNavigatorViewLabelProvider extends LabelProvider {

	public String getText(Object obj) {
		return obj.toString();
	}

	public Image getImage(Object obj) {
		if (obj instanceof Folder){
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		}else if(obj instanceof Item){
			Item item = (Item) obj;
			ItemWrapper itemWrapper = DefaultItemProvider.getNavigatorByType(item);
			String iconPath = itemWrapper.getHelper().getIcon(item);
			
			String regKey = item.getPluginId() + "_" + iconPath;
			if (itemWrapper.getHelper().getIcon(item) != null) {
				ImageRegistry registry = PerformanceActivator.getDefault().getImageRegistry();
				if (registry.getDescriptor(regKey) == null) {
					ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(item.getPluginId(), iconPath);
					registry.put(regKey, descriptor);
				}
				return registry.get(regKey);
	                
//				return PerformanceActivator.getImageDescriptor(itemWrapper.getHelper().getIcon(item)).createImage();
			}
		}
		return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}
}