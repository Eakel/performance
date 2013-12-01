package com.easyfun.eclipse.performance.trace.ui;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;

import com.easyfun.eclipse.common.tree.model.ITreeModel;
import com.easyfun.eclipse.performance.trace.TraceActivator;
import com.easyfun.eclipse.performance.trace.item.trace.TraceNode;


/**
 * Trace目录层次的Tree
 * @author linzhaoming
 *
 * 2013-4-7
 *
 */
public class TraceFileTreeLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
		if(element instanceof ITreeModel){
			ITreeModel model = (ITreeModel)element;
			if(Workbench.getInstance() != null){
				//main函数启动，不存在Workbench
				if(model.isDirectory()){
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
				}else{
					if(model instanceof TraceNode && ((TraceNode)model).getAppTrace() != null){
						return TraceActivator.getImageDescriptor("icons/trace/book.gif").createImage();
					} else {
						return TraceActivator.getImageDescriptor("icons/trace/property.gif").createImage();
					}
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
