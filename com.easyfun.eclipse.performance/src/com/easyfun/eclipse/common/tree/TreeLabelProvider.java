package com.easyfun.eclipse.common.tree;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;

import com.easyfun.eclipse.common.tree.model.ITreeModel;


/**
 * @author linzhaoming
 * Create Date: 2010-8-12
 */
public class TreeLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
		if(element instanceof ITreeModel){
			ITreeModel pair = (ITreeModel)element;
			if(Workbench.getInstance() != null){
				//main函数启动，不存在Workbench
				if(pair.isDirectory()){
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
				}else{
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
				}
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
