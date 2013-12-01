package com.easyfun.eclipse.performance.http.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

import com.easyfun.eclipse.performance.http.model.interfaces.IHttpMethod;

/**
 * The table for Response.
 * 
 * @author linzhaoming
 * 
 */
public class ResponseHeaderTable extends HttpHeaderTable {

	public ResponseHeaderTable(Composite parent, int style) {
		super(parent, style);
		
	}

	protected void hookTableViewer(TableViewer tableViewer, Composite parent) {
		tableViewer.setContentProvider(new ResponseContentProvider(tableViewer));
	}

	private static class ResponseContentProvider implements
			IStructuredContentProvider, PropertyChangeListener {
		private TableViewer tableViewer;

		public ResponseContentProvider(TableViewer tableViewer) {
			this.tableViewer = tableViewer;
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			return ((IHttpMethod) parent).getResponseHeaders();
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (oldInput != null) {
				((IHttpMethod) oldInput).removePropertyChangeListener(IHttpMethod.RESPONSE_PROP, this);
			}

			if (newInput != null) {
				((IHttpMethod) newInput).addPropertyChangeListener(IHttpMethod.RESPONSE_PROP, this);
			}
		}

		public void propertyChange(PropertyChangeEvent evt) {
			tableViewer.refresh();
		}

	}

}
