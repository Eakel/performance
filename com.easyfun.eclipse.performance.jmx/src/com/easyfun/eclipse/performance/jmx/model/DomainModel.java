package com.easyfun.eclipse.performance.jmx.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import com.easyfun.eclipse.component.tree.model.ITreeModel;


/**
 * ´ú±íJMX Domain
 * @author linzhaoming
 * Create Date: 2010-8-14
 */
public class DomainModel extends AbstractMBeanModel implements Comparable {
	
	private String domainName;
	
	public DomainModel(MBeanServerConnection mbeanServerConnection, ObjectName objectName){
		super(mbeanServerConnection, objectName);
		this.domainName = objectName.getDomain();
	}

	public Collection getChildren() {
		try {
			Set set = mbeanServerConnection.queryNames(new ObjectName(domainName + ":*"), null);
			Set retSet = new TreeSet();
			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				ObjectName objectName = (ObjectName) iterator.next();
				MBeanModel mbeanModel = new MBeanModel(this, mbeanServerConnection, objectName);
				retSet.add(mbeanModel);
			}
			return retSet;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ArrayList();
	}

	public Object getInstance() {
		return domainName;
	}

	public ITreeModel getParent() {
		return null;
	}

	public boolean isDirectory() {
		return true;
	}

	public int compareTo(Object o) {
		DomainModel model = (DomainModel)o;
		return domainName.compareTo(model.getDisplayName());
	}
	
	public String getDisplayName(){
		return domainName;
	}

	public Object getType() {
		return mbeanServerConnection;
	}
	
	
}
