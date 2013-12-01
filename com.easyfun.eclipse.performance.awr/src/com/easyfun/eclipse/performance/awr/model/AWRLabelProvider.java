package com.easyfun.eclipse.performance.awr.model;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


public class AWRLabelProvider extends LabelProvider implements ITableLabelProvider{

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object object, int paramInt){
		if(object instanceof SnapShot){
			SnapShot snapshot = (SnapShot)object;
			switch(paramInt){
			case 0:
				return String.valueOf(snapshot.getSnapId());
			case 1:
				return snapshot.getBeginDate();
			case 2:
				return snapshot.getEndDate();
			case 3:
				return String.valueOf(snapshot.getDbId());
			case 4:
				return String.valueOf(snapshot.getInstanceNumber());
			case 5:
				return snapshot.getInstanceName();
			case 6:
				return snapshot.getVersion();
			case 7:
				return snapshot.getHostname();
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