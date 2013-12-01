package com.easyfun.eclipse.performance.http.model.interfaces;

import java.io.InputStream;

import org.apache.commons.httpclient.methods.RequestEntity;

/**
 * SubClasses are:
 * 
 *  <li>HttpPostMethod</li>
 *  <li>HttpPutMethod</li>
 * @author linzhaoming
 *
 */
public interface IEntityEnclosingMethod extends IExpectContinueMethod {
    /**
     * Entity enclosing requests cannot be redirected without user intervention
     * according to RFC 2616.
     *
     * @return <code>false</code>.
     *
     * @since 2.0
     */
    public boolean getFollowRedirects();

    /**
     * Entity enclosing requests cannot be redirected without user intervention 
     * according to RFC 2616.
     *
     * @param followRedirects must always be <code>false</code>
     */
    public void setFollowRedirects(boolean followRedirects);

    /**
     * Sets length information about the request body.
     *
     * <p>
     * Note: If you specify a content length the request is unbuffered. This
     * prevents redirection and automatic retry if a request fails the first
     * time. This means that the HttpClient can not perform authorization
     * automatically but will throw an Exception. You will have to set the
     * necessary 'Authorization' or 'Proxy-Authorization' headers manually.
     * </p>
     *
     * @param length size in bytes or any of CONTENT_LENGTH_AUTO,
     *        CONTENT_LENGTH_CHUNKED. If number of bytes or CONTENT_LENGTH_CHUNKED
     *        is specified the content will not be buffered internally and the
     *        Content-Length header of the request will be used. In this case
     *        the user is responsible to supply the correct content length.
     *        If CONTENT_LENGTH_AUTO is specified the request will be buffered
     *        before it is sent over the network.
     * 
     * @deprecated Use {@link #setContentChunked(boolean)} or 
     * {@link #setRequestEntity(RequestEntity)}
     */
    public void setRequestContentLength(int length);

    /**
     * Returns the request's charset.  The charset is parsed from the request entity's 
     * content type, unless the content type header has been set manually. 
     * 
     * @see RequestEntity#getContentType()
     * 
     * @since 3.0
     */
    public String getRequestCharSet();

    /**
     * Sets length information about the request body.
     *
     * <p>
     * Note: If you specify a content length the request is unbuffered. This
     * prevents redirection and automatic retry if a request fails the first
     * time. This means that the HttpClient can not perform authorization
     * automatically but will throw an Exception. You will have to set the
     * necessary 'Authorization' or 'Proxy-Authorization' headers manually.
     * </p>
     *
     * @param length size in bytes or any of CONTENT_LENGTH_AUTO,
     *        CONTENT_LENGTH_CHUNKED. If number of bytes or CONTENT_LENGTH_CHUNKED
     *        is specified the content will not be buffered internally and the
     *        Content-Length header of the request will be used. In this case
     *        the user is responsible to supply the correct content length.
     *        If CONTENT_LENGTH_AUTO is specified the request will be buffered
     *        before it is sent over the network.
     * 
     * @deprecated Use {@link #setContentChunked(boolean)} or 
     * {@link #setRequestEntity(RequestEntity)}
     */
    public void setRequestContentLength(long length);

    /**
     * Sets whether or not the content should be chunked.
     * 
     * @param chunked <code>true</code> if the content should be chunked
     * 
     * @since 3.0
     */
    public void setContentChunked(boolean chunked);
 
    /**
     * Sets the request body to be the specified inputstream.
     *
     * @param body Request body content as {@link java.io.InputStream}
     * 
     * @deprecated use {@link #setRequestEntity(RequestEntity)}
     */
    public void setRequestBody(InputStream body);

    /**
     * Sets the request body to be the specified string.
     * The string will be submitted, using the encoding
     * specified in the Content-Type request header.<br>
     * Example: <code>setRequestHeader("Content-type", "text/xml; charset=UTF-8");</code><br>
     * Would use the UTF-8 encoding.
     * If no charset is specified, the 
     * {@link org.apache.commons.httpclient.HttpConstants#DEFAULT_CONTENT_CHARSET default}
     * content encoding is used (ISO-8859-1).
     *
     * @param body Request body content as a string
     * 
     * @deprecated use {@link #setRequestEntity(RequestEntity)}
     */
    public void setRequestBody(String body);

    /**
     * Recycles the HTTP method so that it can be used again.
     * Note that all of the instance variables will be reset
     * once this method has been called. This method will also
     * release the connection being used by this HTTP method.
     * 
     * @see #releaseConnection()
     * 
     * @deprecated no longer supported and will be removed in the future
     *             version of HttpClient
     */
    public void recycle();

    /**
     * @return Returns the requestEntity.
     * 
     * @since 3.0
     */
    public RequestEntity getRequestEntity();

    /**
     * @param requestEntity The requestEntity to set.
     * 
     * @since 3.0
     */
    public void setRequestEntity(RequestEntity requestEntity);

}
