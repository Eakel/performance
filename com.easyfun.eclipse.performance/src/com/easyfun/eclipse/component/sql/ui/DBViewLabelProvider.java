package com.easyfun.eclipse.component.sql.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


/**
 * µ¼º½Ê÷LabelProvider
 * @author linzhaoming
 *
 * 2013-12-2
 *
 */
public class DBViewLabelProvider extends LabelProvider {

	public String getText(Object obj) {
		return obj.toString();
	}

	public Image getImage(Object obj) {
		if (obj instanceof DBFolder){
			return new Image(Display.getCurrent(), "icons/folderOpened.gif"); //PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		}else if(obj instanceof DBItem){
			return new Image(Display.getCurrent(), "icons/item.gif");
		}else{
			return null;
		}
	}
}