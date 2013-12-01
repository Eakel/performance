package com.easyfun.eclipse.performance.jmx.model;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import com.easyfun.eclipse.utils.model.tree.ITreeModel;


/**
 * JMX MBean≥ÈœÛ¿‡
 * 
 * @author linzhaoming
 * Create Date: 2010-8-14
 */
public abstract class AbstractMBeanModel implements ITreeModel{
	protected MBeanServerConnection mbeanServerConnection;
	protected ObjectName objectName;
	
	public AbstractMBeanModel(MBeanServerConnection mbeanServerConnection, ObjectName objectName){
		this.mbeanServerConnection = mbeanServerConnection;
		this.objectName = objectName;
	}

	public MBeanServerConnection getConnection() {
		return mbeanServerConnection;
	}

	public ObjectName getObjectName() {
		return objectName;
	}

	public void setObjectName(ObjectName objectName) {
		this.objectName = objectName;
	}
	
	
}
