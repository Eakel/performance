package com.easyfun.eclipse.common.tree;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author linzhaoming Create Date: 2010-8-14
 */
public class TreeViewerFactory {
	
	/**
	 * remember to setInput()
	 * @param parent
	 * @return
	 */
	public static TreeViewer createTreeViewer(Composite parent) {
		final TreeViewer treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		treeViewer.setLabelProvider(new TreeLabelProvider());
		treeViewer.setContentProvider(new TreeContentProvider());
		return treeViewer;
	}
}
