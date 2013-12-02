package com.easyfun.eclipse.common.view.item.navigator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.easyfun.eclipse.common.config.cfg.Folder;
import com.easyfun.eclipse.common.config.cfg.Item;
import com.easyfun.eclipse.common.config.cfg.Navigator;

/**
 * µ¼º½Ê÷ContentProvider
 * @author linzhaoming
 *
 * 2013-12-2
 *
 */
public class ItemNavigatorViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof Item) {
			return ((Item) child).getFolder();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		if(parent instanceof Navigator){
			return ((Navigator)parent).getFolders();
		}
		if (parent instanceof Folder) {
			return ((Folder) parent).getItems();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof Folder) {
			return ((Folder) parent).getItems().length != 0;
		}
		return false;
	}
}