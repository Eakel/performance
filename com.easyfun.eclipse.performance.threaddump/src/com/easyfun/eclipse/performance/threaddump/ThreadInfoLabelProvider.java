package com.easyfun.eclipse.performance.threaddump;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.easyfun.eclipse.performance.threaddump.parser.IThreadState;
import com.easyfun.eclipse.performance.threaddump.parser.ThreadInfo;

/**
 * 
 * @author zhaoming
 *
 * 2011-9-15
 * @deprecated 没有使用
 */
public class ThreadInfoLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {
	
	//实现隔行换色功能
	private Color[] bg = new Color[]{new Color(null, 255,255,255), new Color(null, 247,247,240)};
    private Color[] force = new Color[]{new Color(null, 0,0,0), new Color(null, 0,0,0)};
    private Object current = null;
    private int currentColor = 0;
    
	
	public Color getBackground(Object element, int columnIndex) {
		if (current != element) {
            currentColor = 1 - currentColor;
            current = element;
        }
        return bg[currentColor];
	}

	public Color getForeground(Object element, int columnIndex) {
		return force[currentColor];
	}

	public Image getColumnImage(Object element, int columnIndex) {
		if(element instanceof ThreadInfo){
			ThreadInfo keyValue = (ThreadInfo)element;
			switch(columnIndex){
			case 0:
				IThreadState state = keyValue.getState();
				return state.getImage();
			case 1:
				return null;
			}
		}
		return null;
	}

	public String getColumnText(Object object, int paramInt){
		if(object instanceof ThreadInfo){
			ThreadInfo keyValue = (ThreadInfo)object;
			switch(paramInt){
			case 1:
				return keyValue.getName();

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
