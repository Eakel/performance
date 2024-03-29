package com.easyfun.eclipse.util;

import java.lang.reflect.Field;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Class Util
 * 
 * @author linzhaoming
 *
 */
public class ClassUtil {
	
	   /**
     *Retrieves a Field object for a given field on the specified class, having
     *set it accessible.
     *@param cls the Class on which the field is expected to be defined
     *@param fieldName the name of the field of interest
     *@throws NoSuchFieldException in case of any error retriving information about the field
     */
    public static Field getField(Class<?> cls, String fieldName) throws NoSuchFieldException {
        try {
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException nsfe) {
            NoSuchFieldException e = new NoSuchFieldException("classloaderutil.errorGettingField");
            e.initCause(nsfe);
            throw e;
        }
    }
    
    /**
     *Retrieves a given inner class definition from the specified outer class.
     *@param cls the outer Class
     *@param innerClassName the fully-qualified name of the inner class of interest
     */
    public static Class<?> getInnerClass(Class<?> cls, String innerClassName) {
        Class<?> result = null;
        Class<?>[] innerClasses = cls.getDeclaredClasses();
        for (int index=0;index<innerClasses.length;index++) {
        	Class<?> c = innerClasses[index];
            if (c.getName().equals(innerClassName)) {
                result = c;
                break;
            }
        }
        return result;
    }  
    
	/** ����ָ��Bundle��Class*/
	public static Class loadBundleClass(String bundleId, String className) throws Exception{
		Bundle bundle = Platform.getBundle(bundleId);
		Class clazz = bundle.loadClass(className);
		return clazz;
	}
}
