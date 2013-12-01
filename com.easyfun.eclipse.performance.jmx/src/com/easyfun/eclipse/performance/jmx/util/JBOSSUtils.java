package com.easyfun.eclipse.performance.jmx.util;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.easyfun.eclipse.performance.jmx.model.DomainModel;


public class JBOSSUtils {
	
	/**
	 * Get the JBOSS InitialContext
	 * @return
	 * @throws NamingException
	 */
	public static InitialContext getJBOSSContext(String providerURL) throws NamingException{
		Properties props = new Properties(System.getProperties());
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
//		props.put(Context.PROVIDER_URL, "localhost:1099");
		props.put(Context.PROVIDER_URL, providerURL);
		props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");

		InitialContext ctx = new InitialContext(props);
		return ctx;
	}
	
	/**
	 * Get the JBOSS MBeanServerConnection
	 * @return
	 * @throws NamingException
	 */
	public static MBeanServerConnection getJBOSSMBeanServer(String providerURL) throws Exception{
		try {
			InitialContext ctx = getJBOSSContext(providerURL);
			Object obj = ctx.lookup("jmx/invoker/RMIAdaptor");
			if(obj instanceof MBeanServerConnection){
				return (MBeanServerConnection)obj;
			}else{
				System.err.println("The class is not corrent" + obj.getClass().getName());
				return null;
			}
		} catch (NamingException e) {
			throw e;
		}
	}
	
	/**
	 * Get the JBOSS JMX Domains
	 * @param beanServer
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public static Set getDomains( MBeanServerConnection beanServer, String filter) {
		Set set = new TreeSet();
		try {
			MBeanServerConnection server = beanServer;
			if (server != null) {
				ObjectName filterName = null;
				if(filter!=null && !filter.equals("")){
					filterName = new ObjectName(filter);
				}				
				Set names = server.queryNames(filterName, null);

				for (Iterator iter = names.iterator(); iter.hasNext();) {
					ObjectName objectName = (ObjectName) iter.next();
					DomainModel domainData = new DomainModel(beanServer, objectName);
					set.add(domainData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new TreeSet();
		}
		return set;
	}
	
}
