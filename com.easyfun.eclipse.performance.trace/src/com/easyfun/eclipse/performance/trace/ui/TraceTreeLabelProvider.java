package com.easyfun.eclipse.performance.trace.ui;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;

import com.easyfun.eclipse.common.ui.tree.model.ITreeModel;
import com.easyfun.eclipse.performance.trace.TraceActivator;
import com.easyfun.eclipse.performance.trace.model.ITrace;


/**
 * 
 * @author linzhaoming
 *
 * 2013-4-6
 *
 */
public class TraceTreeLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
		if (element instanceof ITreeModel) {
			ITreeModel pair = (ITreeModel) element;
			if (element instanceof ITrace) {
				if (Workbench.getInstance() != null) {
					// main函数启动，不存在Workbench
					ITrace trace = (ITrace)element;
					String img = TraceUIFactory.getImageByType(trace);
					if (pair.isDirectory()) {
						return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
//						return TraceActivator.getImageDescriptor(img).createImage();
					} else {
//						return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
						return TraceActivator.getImageDescriptor(img).createImage();
					}
				}else{
					ITrace trace = (ITrace)element;
					String img = TraceUIFactory.getImageByType(trace);
					return new Image(Display.getDefault(), img);
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
