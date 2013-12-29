package com.easyfun.eclipse.component.tree2;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 * @author linzhaoming
 * Create Date: 2010-11-27
 */
public class TreeContentProvider implements ITreeContentProvider {

	public TreeContentProvider() {

	}

	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof Collection){
			return ((Collection)inputElement).toArray();
		}
		if (inputElement instanceof ITreeModel) {
			ITreeModel model = (ITreeModel) inputElement;
			return model.getChildren().toArray();
		}else {
			return new Object[0];
		}
	}

	public void dispose() {

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	public Object getParent(Object element) {
		if(element instanceof ITreeModel){
			return ((ITreeModel)element).getParent();
		}
		

		return null;
	}

	public boolean hasChildren(Object element) {
		if(element == null){
			return false;
		}
		if(element instanceof ITreeModel){
			ITreeModel model = (ITreeModel)element;

			Collection c = model.getChildren();
			
			return (c!=null && c.size()>0);
		}
		
		return false;
	}

}