package com.easyfun.eclipse.performance.http.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.performance.http.model.interfaces.IEntityEnclosingMethod;
import com.easyfun.eclipse.performance.http.model.interfaces.IHttpMethod;
import com.easyfun.eclipse.performance.navigator.console.LogHelper;

/**
 * The Http Client View Part.
 * 
 * @author linzhaoming
 *
 */
public class HttpViewPart extends ViewPart implements PropertyChangeListener {
	private RequestStartLine requestStartLine = null;
	private ResponseStatusLine responseStatusLine = null;
	
	private RequestHeaderTable requestHeaderTable = null;
	private ResponseHeaderTable responseHeaderTable = null;
	
	private HttpMethodControl httpMethodControl = null;
	
	/** The text area for Request Body.*/
	private Text requestBodyText = null;
	
	/** The text area for Response Body*/
	private Text responseBodyText = null;	
	
	private Composite responseComposite;
	private Browser browser;
	
	private static final Log log = LogFactory.getLog(HttpViewPart.class);

	public HttpViewPart() {
	}

	public void createPartControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		httpMethodControl = createTopComposite(container);
				
		final CTabFolder tabFolder = new CTabFolder(container, SWT.BORDER | SWT.TOP);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tabFolder.setLayout(new GridLayout());
		tabFolder.setSimple(false);
		tabFolder.setUnselectedImageVisible(false);
		tabFolder.setUnselectedCloseVisible(false);
		
		CTabItem requestTabItem = new CTabItem(tabFolder, SWT.CLOSE);
		requestTabItem.setText("Http Request");
		Composite requestComposite = createRequestComposite(tabFolder);		
		requestTabItem.setControl(requestComposite);
		
		CTabItem responseTabItem = new CTabItem(tabFolder, SWT.CLOSE);
		responseTabItem.setText("Http Response");		
		this.responseComposite = createResponseComposite(tabFolder);		
		responseTabItem.setControl(responseComposite);
		
		tabFolder.setSelection(requestTabItem);
		
		tabFolder.setMinimizeVisible(true);
		tabFolder.setMaximizeVisible(true);
		tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			public void minimize(CTabFolderEvent event) {
				tabFolder.setMinimized(true);
				tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
				container.layout(true);
			}
			public void maximize(CTabFolderEvent event) {
				tabFolder.setMaximized(true);
				tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				container.layout(true);
			}
			public void restore(CTabFolderEvent event) {
				tabFolder.setMinimized(false);
				tabFolder.setMaximized(false);
				tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
				container.layout(true);
			}

			public void close(CTabFolderEvent event) {
				super.close(event);
				event.doit = false;
			}
		});		
		
		refresh();
	}
	
	/**
	 * TOP Composite：请求方法
	 */
	private HttpMethodControl createTopComposite(Composite parent){
		HttpMethodControl httpMethodControl = new HttpMethodControl(parent, SWT.NONE);
		httpMethodControl.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		httpMethodControl.addPropertyChangeListener(this);
		return httpMethodControl;
	}
	
	/** 请求 Composite. */
	private Composite createRequestComposite(Composite parent){
		Composite requestComposite = new Composite(parent, SWT.NONE);
		requestComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		requestComposite.setLayout(new GridLayout(1, false));		

		this.requestStartLine = new RequestStartLine(requestComposite, SWT.NONE);		
		this.requestStartLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		SashForm requestSashForm = new SashForm(requestComposite, SWT.VERTICAL);
		requestSashForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		this.requestHeaderTable = new RequestHeaderTable(requestSashForm, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData.heightHint = 150;
		this.requestHeaderTable.setLayoutData(gridData);
		
		this.requestBodyText = new Text(requestSashForm, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		requestBodyText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.requestBodyText.setEnabled(false);
		return requestComposite;		
	}
	
	/** 相应 Composite */
	private Composite createResponseComposite(Composite parent){
		Composite responseComposite = new Composite(parent, SWT.NONE);
		responseComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		responseComposite.setLayout(new GridLayout());
		
		final CTabFolder tabFolder = new CTabFolder(responseComposite, SWT.BORDER | SWT.BOTTOM);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tabFolder.setLayout(new GridLayout());
		tabFolder.setSimple(false);
		tabFolder.setUnselectedImageVisible(false);
		tabFolder.setUnselectedCloseVisible(false);
		
		tabFolder.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if(tabFolder.getSelection().getText().equals("Browser")){
//					browser.setText(requestBodyText.getText());
				}				
			}
		});
		
		
		CTabItem item1 = new CTabItem(tabFolder, SWT.CLOSE);
		Composite comp1= new Composite(tabFolder, SWT.NULL);
		comp1.setLayoutData(GridData.FILL_BOTH);
		comp1.setLayout(new GridLayout());
		item1.setText("Response");
		item1.setControl(comp1);
		item1.setShowClose(false);
		
		CTabItem item2 = new CTabItem(tabFolder, SWT.CLOSE);
		Composite comp2= new Composite(tabFolder, SWT.NULL);
		comp2.setLayoutData(GridData.FILL_BOTH);
		comp2.setLayout(new GridLayout());
		item2.setText("Browser");
		item2.setControl(comp2);
		item2.setShowClose(false);
		
		CTabItem item3 = new CTabItem(tabFolder, SWT.CLOSE);
		Composite comp3= new Composite(tabFolder, SWT.NULL);
		comp3.setLayoutData(GridData.FILL_BOTH);
		comp3.setLayout(new GridLayout());
		item3.setText("Raw View");
		item3.setControl(comp3);
		item3.setShowClose(false);
		
		CTabItem item4 = new CTabItem(tabFolder, SWT.CLOSE);
		Composite comp4= new Composite(tabFolder, SWT.NULL);
		comp4.setLayoutData(GridData.FILL_BOTH);
		comp4.setLayout(new GridLayout());
		item4.setText("JSON View");
		item4.setControl(comp4);
		item4.setShowClose(false);
		
		tabFolder.setSelection(item1);
		
		//Response
		responseStatusLine = new ResponseStatusLine(comp1, SWT.NONE);
		responseStatusLine.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		SashForm responseSashForm = new SashForm(comp1, SWT.VERTICAL);
		responseSashForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		responseHeaderTable = new ResponseHeaderTable(responseSashForm, SWT.NONE);
		responseBodyText = new Text(responseSashForm, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY|SWT.BORDER);
		
		//Browser
		try {
			browser = new Browser(comp2, SWT.H_SCROLL);
			browser.setLayoutData(new GridData(GridData.FILL_BOTH));			
		} catch (SWTError e) {
			LogHelper.error(log, "Could not instantiate Browser: ");
			e.printStackTrace();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
		return responseComposite;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}

	private void refresh() {
		IHttpMethod httpMethod = httpMethodControl.getMethod();
		
		requestStartLine.setInput(httpMethod);
		requestHeaderTable.setInput(httpMethod);
		responseStatusLine.setInput(httpMethod);
		responseHeaderTable.setInput(httpMethod);

		if (httpMethod instanceof IEntityEnclosingMethod) {	//POST and PUT
			requestBodyText.setEnabled((httpMethod != null));
		} else {
			requestBodyText.setEnabled(false);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if (propertyName.equals(HttpMethodControl.METHOD_PROP)) {
			refresh();
		} else if (propertyName.equals(IHttpMethod.RESPONSE_PROP)) {
			responseBodyText.setText(httpMethodControl.getResponseBody());
			responseComposite.layout();
			browser.setText(httpMethodControl.getResponseBody());
		}
	}

}