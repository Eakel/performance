package com.easyfun.eclipse.performance.http.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.easyfun.eclipse.performance.http.HttpActivator;
import com.easyfun.eclipse.performance.http.HttpImageConstants;
import com.easyfun.eclipse.performance.http.model.HttpMethodEnum;
import com.easyfun.eclipse.performance.http.model.MethodFactory;
import com.easyfun.eclipse.performance.http.model.interfaces.IHttpMethod;

/**
 * 请求方法
 * <li>Http Method Combo and the "Invoke" button.
 * 
 * @author linzhaoming
 *
 */
public class HttpMethodControl extends Composite implements PropertyChangeListener {	
	public static final String METHOD_PROP = "method";
	/** The Buffer size to read the response. */
	private static final int CHAR_BUFF_SIZE = 2048;

	/** 当前请求方法: httpMethod. */
	private IHttpMethod httpMethod = null;

	private HttpURLControl httpURLControl = null;
	
	/** 用于执行HTTP请求的HttpClient*/
	private HttpClient httpClient = new HttpClient();
	
	/** 执行HTTP方法后，获取的相应内容*/
	private String responseBody = null;

	public HttpMethodControl(Composite parent, int style) {
		super(parent, style);
		initialize();
	}
	
	/** 初始化UI */
	private void initialize() {
		this.setLayout(new GridLayout());
		
		Group group = new Group(this, SWT.NONE);
		group.setText("HTTP Method");
		group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 2));
		group.setLayout(new GridLayout(2, false));
		
		final Combo methodCombo = new Combo(group, SWT.READ_ONLY);
		methodCombo.setItems(new String[]{HttpMethodEnum.GET.toString(), HttpMethodEnum.POST.toString(), HttpMethodEnum.HEAD.toString(), 
				HttpMethodEnum.OPTIONS.toString(), HttpMethodEnum.TRACE.toString(), HttpMethodEnum.PUT.toString(), HttpMethodEnum.DELETE.toString()});
		methodCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				IHttpMethod method = createMethod(methodCombo.getText());
				method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
				setMethod(method);
			}
		});
		methodCombo.select(0);
		
		
		httpURLControl = new HttpURLControl(group,SWT.NONE);
		httpURLControl.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 3));
		httpURLControl.addPropertyChangeListener(this);
		
		Button executeButton = new Button(group, SWT.NONE);
		executeButton.setText("Invoke");
		executeButton.setImage(HttpActivator.getImageDescriptor(HttpImageConstants.ICON_RUN_PATH).createImage());
		executeButton.setLayoutData(new GridData(GridData.FILL,GridData.CENTER, false, false));
		executeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (httpMethod != null) {
					execute();
				}
			}
		});
	}
	
	/**
	 * Get the responseBody after invoked the Http Method.
	 */
	public String getResponseBody() {
		return this.responseBody != null?this.responseBody:"";
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource().equals(httpURLControl)) {
			try {
				httpMethod.setURI(httpURLControl.getURL());
				firePropertyChange(METHOD_PROP, null, null);
			} catch (URIException e) {
				e.printStackTrace(); // Should not happend.
			}
		}
		firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
	}
	
	public IHttpMethod getMethod() {
		return this.httpMethod;
	}

	public void setMethod(IHttpMethod method) {
		if (this.httpMethod != null) {
			this.httpMethod.removePropertyChangeListener(this);
		}
		IHttpMethod oldMethod = this.httpMethod;
		this.httpMethod = method;
		this.httpMethod.addPropertyChangeListener(this);
		try {
			if(httpURLControl!=null){
				method.setURI(httpURLControl.getURL());
			}
		} catch (URIException e) {
			e.printStackTrace(); // Should not happen.
		}
		firePropertyChange(METHOD_PROP, oldMethod, method);
	}

	private IHttpMethod createMethod(String methodName) {
		HttpMethodEnum method = HttpMethodEnum.getHttpMethodByName(methodName);
		return MethodFactory.createHttpMethod(method);
	}

	/** 执行HTTP请求*/
	private void execute() {
		try {
			if (httpMethod == null || !httpMethod.validate() || httpMethod.getURI().getHost() == null) {
				return;
			}

			httpClient.executeMethod(httpMethod);
			this.responseBody = getResponseBody(httpMethod);
			httpMethod.firePropertyChange(IHttpMethod.RESPONSE_PROP, null, null);
			httpMethod.firePropertyChange(IHttpMethod.REQUEST_HEADERS_PROP, null, null);
			httpMethod.firePropertyChange(IHttpMethod.PATH_PROP, null, null);
		} catch (Exception e) {
			//执行HTTP请求失败
			this.responseBody = "Request Failed: " + e.getMessage();
			httpMethod.firePropertyChange(IHttpMethod.RESPONSE_PROP, null, null);
		} finally {
			httpMethod.releaseConnection();
		}
	}

	/**
	 * 执行HTTP方法后获取响应内容
	 */
	private String getResponseBody(IHttpMethod httpMethod) {
		try {
			StringBuffer sb = new StringBuffer();
			char[] cbuf = new char[CHAR_BUFF_SIZE];
			Reader reader = new InputStreamReader(httpMethod.getResponseBodyAsStream(), httpMethod.getResponseCharSet());
			int numChars;
			while ((numChars = reader.read(cbuf)) != -1) {
				sb.append(cbuf, 0, numChars);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//Add the property support
	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	//ViewPart call
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