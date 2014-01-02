/**
 * 
 */
package com.easyfun.eclipse.performance.appframe.webtool.bo;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

/**
 * @author zhaoming
 * 
 * Jan 8, 2008
 */
public class StringEditorInput implements IStorageEditorInput {
	private StringStorage storage;

	public StringEditorInput(StringStorage storage) {
		this.storage = storage;
	}

	public IStorage getStorage() throws CoreException {
		return storage;
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return storage.getName();
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return storage.getName();
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

}
