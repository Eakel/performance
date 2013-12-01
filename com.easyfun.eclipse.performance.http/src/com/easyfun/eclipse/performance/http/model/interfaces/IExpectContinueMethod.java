package com.easyfun.eclipse.performance.http.model.interfaces;

import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * 
 * @author linzhaoming
 *
 */
public interface IExpectContinueMethod extends IHttpMethod {
    /**
     * <p>
     * Returns <tt>true</tt> if the 'Expect: 100-Continue' handshake
     * is activated. The purpose of the 'Expect: 100-Continue' 
     * handshake to allow a client that is sending a request message 
     * with a request body to determine if the origin server is 
     * willing to accept the request (based on the request headers) 
     * before the client sends the request body.
     * </p>
     * 
     * @return <tt>true</tt> if 'Expect: 100-Continue' handshake is to
     * be used, <tt>false</tt> otherwise.
     * 
     * @since 2.0beta1
     * 
     * @deprecated Use {@link HttpMethodParams}
     * 
     * @see #getParams()
     * @see HttpMethodParams
     * @see HttpMethodParams#USE_EXPECT_CONTINUE
     */
    public boolean getUseExpectHeader();

    /**
     * <p>
     * Activates 'Expect: 100-Continue' handshake. The purpose of 
     * the 'Expect: 100-Continue' handshake to allow a client that is 
     * sending a request message with a request body to determine if 
     * the origin server is willing to accept the request (based on 
     * the request headers) before the client sends the request body.
     * </p>
     * 
     * <p>
     * The use of the 'Expect: 100-continue' handshake can result in 
     * noticable peformance improvement for entity enclosing requests
     * (such as POST and PUT) that require the target server's 
     * authentication.
     * </p>
     * 
     * <p>
     * 'Expect: 100-continue' handshake should be used with 
     * caution, as it may cause problems with HTTP servers and 
     * proxies that do not support HTTP/1.1 protocol.
     * </p>
     * 
     * @param value boolean value
     * 
     * @since 2.0beta1
     * 
     * @deprecated Use {@link HttpMethodParams}
     * 
     * @see #getParams()
     * @see HttpMethodParams
     * @see HttpMethodParams#USE_EXPECT_CONTINUE
     */
    public void setUseExpectHeader(boolean value);

}
