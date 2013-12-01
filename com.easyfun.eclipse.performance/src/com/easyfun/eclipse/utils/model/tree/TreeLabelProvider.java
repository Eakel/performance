package com.easyfun.eclipse.utils.model.tree;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.easyfun.eclipse.performance.PerformanceActivator;
import com.easyfun.eclipse.utils.common.ImageConstants;


/**
 * @author linzhaoming
 * Create Date: 2010-8-12
 */
public class TreeLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
		if(element instanceof ITreeModel){
			ITreeModel pair = (ITreeModel)element;
			if(pair.isDirectory()){
				return AbstractUIPlugin.imageDescriptorFromPlugin(PerformanceActivator.PLUGIN_ID, ImageConstants.FOLDER_PATH).createImage();
			}else{
				return AbstractUIPlugin.imageDescriptorFromPlugin(PerformanceActivator.PLUGIN_ID, ImageConstants.ITEM_PATH).createImage();
			}
		}
		return null;
	}

	public String getText(Object element) {
		if(element instanceof ITreeModel){
			return ((ITreeModel)element).getDisplayName();
		}
		return super.getText(element);
	}
}
