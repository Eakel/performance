package com.easyfun.eclipse.common.ui;

import org.eclipse.swt.layout.GridLayout;

/**
 * 
 * @author linzhaoming
 *
 * 2012-7-8
 *
 */
public class LayoutUtil {
	/** 四个Margin都为0*/
	public static GridLayout getNoMarginLayout(){
		GridLayout layout = new GridLayout();
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginBottom = 0;
		return layout;
	}
	
	public static GridLayout getNoMarginLayout(int numColumns, boolean makeColumnsEqualWidth){
		GridLayout layout = getNoMarginLayout();
		layout.numColumns = numColumns;
		layout.makeColumnsEqualWidth = makeColumnsEqualWidth;
		return layout;
	}
}
