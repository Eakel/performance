package com.easyfun.eclipse.performance.other.memcached.model;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


public class MemLabelProvider extends LabelProvider implements ITableLabelProvider{

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object object, int paramInt){
		if(object instanceof MemModel){
			MemModel model = (MemModel)object;
			switch(paramInt){
			case 0:
				return model.getKey();
			case 1:
				return model.getValue();
			case 2:
				return model.getDesc();
			}
		}
		return "";
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

}