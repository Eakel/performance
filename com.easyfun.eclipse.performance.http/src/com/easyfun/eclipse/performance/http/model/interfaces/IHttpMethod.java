package com.easyfun.eclipse.performance.http.model.interfaces;

import java.beans.PropertyChangeListener;

import org.apache.commons.httpclient.HttpMethod;

/**
 * 
 * @author linzhaoming
 *
 */
public interface IHttpMethod extends HttpMethod {

	/** �¼�����: ��Ӧ���� ["response"]*/
	public static final String RESPONSE_PROP = "response";
	/** �¼�����: URI ["URI"]*/
	public static final String URI_PROP = "URI";
	/** �¼�����: ·�� ["path"]*/
	public static final String PATH_PROP = "path";
	/** �¼�����: version ["version"]*/
	public static final String VERSION_PROP = "version";
	/** �¼�����: ����ͷ ["requestHeaders"]*/
	public static final String REQUEST_HEADERS_PROP = "requestHeaders";

    /**
     * Returns the character encoding of the request from the <tt>Content-Type</tt> header.
     * 
     * @return String The character set.
     */
    public String getRequestCharSet();

    /**  
     * Returns the character encoding of the response from the <tt>Content-Type</tt> header.
     * 
     * @return String The character set.
     */
    public String getResponseCharSet();

	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue);
}
