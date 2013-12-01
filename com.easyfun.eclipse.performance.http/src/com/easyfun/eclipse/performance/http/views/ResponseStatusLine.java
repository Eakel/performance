package com.easyfun.eclipse.performance.http.views;

import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.performance.http.model.interfaces.IHttpMethod;

/**
 * Http Version | Status Code | Reason Phase
 * @author linzhaoming
 *
 */
public class ResponseStatusLine extends Composite {
	private Text httpVersionText = null;
	private Text statusText = null;
	private Text resonPhaseText = null;

	private ResponseStatusLineViewer responseStatusLineViewer = null;

	public ResponseStatusLine(Composite parent, int style) {
		super(parent, style);
		initialize();
		responseStatusLineViewer.setContentProvider(new StatusContentProvider());
	}

	private void initialize() {
		setLayout(new GridLayout(6, false));
		
		CLabel label = new CLabel(this, SWT.CENTER);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		label.setText("Http Version");
		
		httpVersionText = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		httpVersionText.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

		label = new CLabel(this, SWT.CENTER);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		label.setText("Status Code");
		
		statusText = new Text(this, SWT.BORDER  | SWT.READ_ONLY);
		statusText.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

		statusText.addFocusListener(new org.eclipse.swt.events.FocusAdapter() { 
			String requestURLOnFocus = null;
			public void focusGained(org.eclipse.swt.events.FocusEvent e) {
				requestURLOnFocus = statusText.getText();
			}
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {    
				String currentRequestURL = statusText.getText();
				if (!currentRequestURL.equals(requestURLOnFocus)) {
					IHttpMethod method = (IHttpMethod) responseStatusLineViewer.getInput();
					method.setPath(currentRequestURL);
				}
				requestURLOnFocus = null;
			}
		});

		label = new CLabel(this, SWT.CENTER);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		label.setText("Reason Phase");

		resonPhaseText = new Text(this, SWT.BORDER  | SWT.READ_ONLY);
		resonPhaseText.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

		responseStatusLineViewer = new ResponseStatusLineViewer(this);
	}

	public final void setInput(IHttpMethod method) {
		responseStatusLineViewer.setInput(method);
	}
	
	/**
	 * The ContentViewer for StatusLine.
	 * 
	 * @author linzhaoming
	 *
	 */
	private static class ResponseStatusLineViewer extends ContentViewer {

		private ResponseStatusLine responseStatusLine;

		public ResponseStatusLineViewer(ResponseStatusLine statusLine) {
			this.responseStatusLine = statusLine;
		}

		public Control getControl() {
			return responseStatusLine;
		}

		public ISelection getSelection() {
			return null;
		}

		public void refresh() {
			if (getContentProvider() instanceof IStructuredContentProvider) {
				Object[] elements = ((IStructuredContentProvider) getContentProvider()).getElements(getInput());
				if (elements.length > 0) {
					responseStatusLine.httpVersionText.setText((String) elements[0]);
					responseStatusLine.statusText.setText((String) elements[1]);
					responseStatusLine.resonPhaseText.setText((String) elements[2]);
				}
			}
		}

		public void setSelection(ISelection selection, boolean reveal) {
		}

		protected void inputChanged(Object input, Object oldInput) {
			refresh();
		}

	}
	
	public class StatusContentProvider implements IStructuredContentProvider, PropertyChangeListener  {

		public void dispose() {
		}

		public Object[] getElements(Object inputElement) {
			IHttpMethod method = (IHttpMethod) inputElement;
			if (method.getStatusLine() == null) {
				return new Object[0];
			}
			return new Object[] { method.getStatusLine().getHttpVersion(), String.valueOf(method.getStatusLine().getStatusCode()),
					method.getStatusLine().getReasonPhrase() };
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (oldInput != null) {
				((IHttpMethod)oldInput).removePropertyChangeListener(IHttpMethod.RESPONSE_PROP, this);
			}
			
			if (newInput != null) {
				((IHttpMethod)newInput).addPropertyChangeListener(IHttpMethod.RESPONSE_PROP, this);
			}
		}

		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			responseStatusLineViewer.refresh();
		}
	}

}
