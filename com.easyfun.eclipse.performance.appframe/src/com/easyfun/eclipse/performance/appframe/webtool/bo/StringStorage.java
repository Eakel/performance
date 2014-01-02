package com.easyfun.eclipse.performance.appframe.webtool.bo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

/**
 * @author zhaoming
 * 
 * Jan 8, 2008
 */
public class StringStorage implements IStorage {
	private String string;

	public StringStorage(String string) {
		this.string = string;
	}

	public InputStream getContents() throws CoreException {
		return new ByteArrayInputStream(string.getBytes());
	}

	public IPath getFullPath() {
		return null;
	}

	public String getName() {
		String result=string;
		if(string.length()>5){
			result=string.substring(0, 3).concat("...");
		}
		return result;
	}

	public boolean isReadOnly() {
		return false;
	}

	public Object getAdapter(Class adapter) {
		return null;
	}
}
