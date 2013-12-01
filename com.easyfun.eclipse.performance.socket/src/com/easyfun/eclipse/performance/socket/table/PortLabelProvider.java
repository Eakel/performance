package com.easyfun.eclipse.performance.socket.table;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author linzhaoming
 * Create Date: 2010-12-14
 */
public class PortLabelProvider extends LabelProvider implements ITableLabelProvider{
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof PortModel) {
			PortModel model = (PortModel)element;
			switch (columnIndex) {
			case 0:
				return model.getPort();
			case 1:
				return model.getUse();
			default:
				return model.getDescription();
			}
		} else {
			return element.toString();
		}
	}
}
