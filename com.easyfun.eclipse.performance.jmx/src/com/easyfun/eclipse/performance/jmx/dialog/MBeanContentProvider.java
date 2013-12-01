package com.easyfun.eclipse.performance.jmx.dialog;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.easyfun.eclipse.performance.jmx.model.MBeanModel;

public class MBeanContentProvider implements IStructuredContentProvider {

	public static final int TYPE_ATTRIBUTE = 1;
	public static final int TYPE_OPERATION = 2;

	private int type = TYPE_ATTRIBUTE;

	public MBeanContentProvider(int type) {
		this.type = type;
	}

	public Object[] getElements(Object inputElement) {
		if (type == TYPE_ATTRIBUTE && (inputElement instanceof MBeanModel)) {
			MBeanModel mbeanModel = (MBeanModel) inputElement;
			MBeanServerConnection mbeanServerConnection = mbeanModel
					.getConnection();
			try {
				MBeanInfo set = mbeanServerConnection.getMBeanInfo(mbeanModel
						.getObjectName());
				return set.getAttributes();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (type == TYPE_OPERATION
				&& (inputElement instanceof MBeanModel)) {
			MBeanModel mbeanModel = (MBeanModel) inputElement;
			MBeanServerConnection mbeanServerConnection = mbeanModel
					.getConnection();
			try {
				MBeanInfo set = mbeanServerConnection.getMBeanInfo(mbeanModel
						.getObjectName());
				return set.getOperations();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Object[] { inputElement };
	}

	public void dispose() {

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}