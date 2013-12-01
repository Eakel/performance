package com.easyfun.eclipse.performance.http.model.interfaces;

import java.util.Enumeration;

public interface IOptionsMethod extends IHttpMethod {
    /**
     * Is the specified method allowed ?
     * 
     * @param method The method to check.
     * @return true if the specified method is allowed.
     * @since 1.0
     */
    public boolean isAllowed(String method);


    /**
     * Get a list of allowed methods.
     * @return An enumeration of all the allowed methods.
     *
     * @since 1.0
     */
    public Enumeration getAllowedMethods();

}
