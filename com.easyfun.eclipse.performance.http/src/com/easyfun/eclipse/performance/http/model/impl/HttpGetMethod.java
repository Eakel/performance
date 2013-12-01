package com.easyfun.eclipse.performance.http.model.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.apache.commons.httpclient.methods.GetMethod;

import com.easyfun.eclipse.performance.http.model.interfaces.IGetMethod;

/**
 * 
 * @author linzhaoming
 *
 */
public class HttpGetMethod extends GetMethod implements IGetMethod {
	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
	}

	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

}
