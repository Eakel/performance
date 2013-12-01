package com.easyfun.eclipse.performance.jmx.dialog;

import java.util.Arrays;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author linzhaoming
 * Create Date: 2010-8-14
 */
public class MBeanTableLabelProvider extends LabelProvider implements ITableLabelProvider{

//	public String getText(Object element) {
//		if(element instanceof MBeanAttributeInfo){
//			MBeanAttributeInfo attrInfo = (MBeanAttributeInfo)element;
//			return "Name=" + attrInfo.getName() + ", Type=" + attrInfo.getType()
//			+ "|" + attrInfo.isReadable() + "|" + attrInfo.isWritable() + "|" + attrInfo.isIs();
//		}
//		return super.getText(element);
//	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof MBeanAttributeInfo) {
			MBeanAttributeInfo attrInfo = (MBeanAttributeInfo)element;
			switch (columnIndex) {
			case 0:
				return attrInfo.getName();
			case 1:
				return attrInfo.getType(); 
			case 2:
				String str = "";
				if(attrInfo.isIs() || attrInfo.isReadable()){
					str += "R";
				}
				
				if(attrInfo.isWritable()){
					str += "W";
				}
				return str;
			case 3:
//				return attrin
				return attrInfo.toString();
			case 4:
				return attrInfo.getDescription();
			default:
				break;
			}
		}else if(element instanceof MBeanOperationInfo){
			MBeanOperationInfo operInfo = (MBeanOperationInfo)element;
			switch (columnIndex) {
			case 0:
				return operInfo.getName();
			case 1:
				return operInfo.getReturnType();
			case 2:
				return operInfo.getDescription();
			case 3:
				return Arrays.asList(operInfo.getSignature()).toString();
			case 4:
				return operInfo.getDescription();
			default:
				break;
			}
		}
		return element.toString();
	}
}
