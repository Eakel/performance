package com.easyfun.eclipse.performance.http.model.interfaces;

import org.apache.commons.httpclient.NameValuePair;

public interface IPostMethod extends IEntityEnclosingMethod {
    /**
     * Sets the value of parameter with parameterName to parameterValue. This method
     * does not preserve the initial insertion order.
     *
     * @param parameterName name of the parameter
     * @param parameterValue value of the parameter
     *
     * @since 2.0
     */
    public void setParameter(String parameterName, String parameterValue);


    /**
     * Gets the parameter of the specified name. If there exists more than one
     * parameter with the name paramName, then only the first one is returned.
     *
     * @param paramName name of the parameter
     *
     * @return If a parameter exists with the name argument, the coresponding
     *         NameValuePair is returned.  Otherwise null.
     *
     * @since 2.0
     * 
     */
    public NameValuePair getParameter(String paramName);

    /**
     * Gets the parameters currently added to the PostMethod. If there are no
     * parameters, a valid array is returned with zero elements. The returned
     * array object contains an array of pointers to  the internal data
     * members.
     *
     * @return An array of the current parameters
     *
     * @since 2.0
     * 
     */
    public NameValuePair[] getParameters();
    
    /**
     * Adds a new parameter to be used in the POST request body.
     *
     * @param paramName The parameter name to add.
     * @param paramValue The parameter value to add.
     *
     * @throws IllegalArgumentException if either argument is null
     *
     * @since 1.0
     */
    public void addParameter(String paramName, String paramValue) 
    throws IllegalArgumentException;
    
    /**
     * Adds a new parameter to be used in the POST request body.
     *
     * @param param The parameter to add.
     *
     * @throws IllegalArgumentException if the argument is null or contains
     *         null values
     *
     * @since 2.0
     */
    public void addParameter(NameValuePair param) 
    throws IllegalArgumentException;

    /**
     * Adds an array of parameters to be used in the POST request body. Logs a
     * warning if the parameters argument is null.
     *
     * @param parameters The array of parameters to add.
     *
     * @since 2.0
     */
    public void addParameters(NameValuePair[] parameters);

    /**
     * Removes all parameters with the given paramName. If there is more than
     * one parameter with the given paramName, all of them are removed.  If
     * there is just one, it is removed.  If there are none, then the request
     * is ignored.
     *
     * @param paramName The parameter name to remove.
     *
     * @return true if at least one parameter was removed
     *
     * @throws IllegalArgumentException When the parameter name passed is null
     *
     * @since 2.0
     */
    public boolean removeParameter(String paramName) 
    throws IllegalArgumentException;
    
    /**
     * Removes all parameter with the given paramName and paramValue. If there
     * is more than one parameter with the given paramName, only one is
     * removed.  If there are none, then the request is ignored.
     *
     * @param paramName The parameter name to remove.
     * @param paramValue The parameter value to remove.
     *
     * @return true if a parameter was removed.
     *
     * @throws IllegalArgumentException when param name or value are null
     *
     * @since 2.0
     */
    public boolean removeParameter(String paramName, String paramValue) 
    throws IllegalArgumentException;

    /**
     * Sets an array of parameters to be used in the POST request body
     *
     * @param parametersBody The array of parameters to add.
     *
     * @throws IllegalArgumentException when param parameters are null
     * 
     * @since 2.0beta1
     */
    public void setRequestBody(NameValuePair[] parametersBody)
    throws IllegalArgumentException;
}
