package com.easyfun.eclipse.performance.trace.builder;

import org.dom4j.Element;

import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.performance.trace.model.ITrace;
import com.easyfun.eclipse.performance.trace.model.SimpleParam;


public interface IBuilder {
	public static final String PRE_STR = "<![CDATA[";
	public static final String LST_STR = "]]>";

	/** ½âÎö¸ù AppTrace*/
	public AppTrace parseAppTrace(Element elemt) throws Exception;
	
	public ITrace parseMdbTrace(Element elemt) throws Exception;

	public ITrace parseMemTrace(Element elemt) throws Exception;

	public ITrace parseHttpTrace(Element elemt) throws Exception;

	public ITrace parseWsTrace(Element elemt) throws Exception;

	public ITrace parseJdbcTrace(Element elemt) throws Exception;

	public ITrace parseDaoTrace(Element elemt) throws Exception;

	public ITrace parseSvrTrace(Element elemt) throws Exception;

	public ITrace parseWebTrace(Element elemt) throws Exception;

	public SimpleParam parseParam(Element elemt) throws Exception;


}