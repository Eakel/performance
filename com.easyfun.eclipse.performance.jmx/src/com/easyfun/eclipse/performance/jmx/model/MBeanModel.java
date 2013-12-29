package com.easyfun.eclipse.performance.jmx.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import com.easyfun.eclipse.common.ui.tree2.ITreeModel;

/**
 * ´ú±íJMX MBean
 * 
 * @author linzhaoming
 * Create Date: 2010-8-14
 */
public class MBeanModel extends AbstractMBeanModel implements Comparable {
	
	private DomainModel domainModel;

	public MBeanModel(DomainModel domainModel, MBeanServerConnection mbeanServerConnection, ObjectName objectName){
		super(mbeanServerConnection, objectName);
		this.domainModel = domainModel;
	}
	
	public Collection getChildren() {
		return new ArrayList();
	}

	public Object getInstance() {
		return objectName;
	}

	public ITreeModel getParent() {
		return domainModel;
	}

	public boolean isDirectory() {
		return false;
	}

	public int compareTo(Object obj) {
		MBeanModel md = (MBeanModel) obj;
		String d1 = objectName.getDomain();
		String d2 = md.objectName.getDomain();
		int compare = d1.compareTo(d2);
		if (compare == 0) {
			String p1 = objectName.getCanonicalKeyPropertyListString();
			String p2 = md.objectName.getCanonicalKeyPropertyListString();
			compare = p1.compareTo(p2);
		}
		return compare;
	}
	
	public String getDisplayName(){
		return objectName.getCanonicalName();
	}
	
	public ObjectName getObjectName(){
		return objectName;
	}
}
