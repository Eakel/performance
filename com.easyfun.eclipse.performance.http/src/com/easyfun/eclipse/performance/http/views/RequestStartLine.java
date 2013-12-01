package com.easyfun.eclipse.performance.http.views;

import java.beans.PropertyChangeListener;

import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.ProtocolException;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.performance.http.model.interfaces.IHttpMethod;

/**
 * Request:
 * <p>Method | Path | Http Version.
 * @author linzhaoming
 *
 */
public class RequestStartLine extends Composite {
	private RequestStartLineViewer requestStartLineViewer = null;
	
	private Text httpMethodText = null;
	private Text requestURLText = null;
	private Combo versionCombo = null;

	public RequestStartLine(Composite parent, int style) {
		super(parent, style);
		initialize();
		requestStartLineViewer.setContentProvider(new RequestStartLineContentProvider(requestStartLineViewer));
	}

	/**
	 * Initialize the UI.
	 */
	private void initialize() {
		this.setLayout(new GridLayout(6,false));
		CLabel label = new CLabel(this, SWT.CENTER);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		label.setText("Method: ");

		httpMethodText = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		GridData gridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gridData.widthHint = 100;
		httpMethodText.setLayoutData(gridData);
		
		label = new CLabel(this, SWT.CENTER);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));
		label.setText("HTTP Version: ");
		
		versionCombo = new Combo(this, SWT.READ_ONLY);
		versionCombo.add(HttpVersion.HTTP_0_9.toString());
		versionCombo.add(HttpVersion.HTTP_1_0.toString());
		versionCombo.add(HttpVersion.HTTP_1_1.toString());
		versionCombo.setEnabled(false);
		versionCombo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		versionCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				HttpVersion version = null;
				try {
					version = HttpVersion.parse(versionCombo.getText());
				} catch (ProtocolException e1) {
					version = HttpVersion.HTTP_1_1;
				}
				IHttpMethod method = (IHttpMethod) requestStartLineViewer.getInput();
				method.getParams().setVersion(version);
			}
		});
		
		label = new CLabel(this, SWT.CENTER);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		label.setText("Path");
		
		requestURLText = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		requestURLText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		requestStartLineViewer = new RequestStartLineViewer(this);
	}	

	public final void setInput(IHttpMethod method) {
		requestStartLineViewer.setInput(method);
		versionCombo.setEnabled(method != null);
	}
	
	private static class RequestStartLineContentProvider implements IStructuredContentProvider, PropertyChangeListener  {
		private RequestStartLineViewer startLineViewer = null;
		
		public RequestStartLineContentProvider(RequestStartLineViewer startLineViewer){
			this.startLineViewer = startLineViewer;
		}
		public void dispose() {
		}

		public Object[] getElements(Object inputElement) {
			IHttpMethod method = (IHttpMethod) inputElement;
			return new Object[] { method.getName(), method.getPath(),
					method.getParams().getVersion().toString() };
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (oldInput != null) {
				((IHttpMethod)oldInput).removePropertyChangeListener(IHttpMethod.PATH_PROP, this);
				((IHttpMethod)oldInput).removePropertyChangeListener(IHttpMethod.VERSION_PROP, this);
			}
			
			if (newInput != null) {
				((IHttpMethod)newInput).addPropertyChangeListener(IHttpMethod.PATH_PROP, this);
				((IHttpMethod)newInput).addPropertyChangeListener(IHttpMethod.VERSION_PROP, this);
			}
		}

		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			startLineViewer.refresh();
		}
	}
	
	/**
	 * ContentViewer for StartLine.
	 * 
	 * @author linzhaoming
	 *
	 */
	private static class RequestStartLineViewer extends ContentViewer {
		private RequestStartLine requestStartLine;

		public RequestStartLineViewer(RequestStartLine startLine) {
			this.requestStartLine = startLine;
		}

		public Control getControl() {
			return requestStartLine;
		}

		public ISelection getSelection() {
			return null;
		}

		public void refresh() {
			if (getContentProvider() instanceof IStructuredContentProvider) {
				Object[] elements = ((IStructuredContentProvider) getContentProvider()).getElements(getInput());
				requestStartLine.httpMethodText.setText((String) elements[0]);				
				requestStartLine.requestURLText.setText((String) elements[1]);
				requestStartLine.versionCombo.setText((String) elements[2]);
			}
		}

		public void setSelection(ISelection selection, boolean reveal) {
		}

		protected void inputChanged(Object input, Object oldInput) {
			refresh();
		}
	}
}