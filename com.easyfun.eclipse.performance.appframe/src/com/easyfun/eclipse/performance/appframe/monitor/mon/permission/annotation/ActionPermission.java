package com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ActionPermission {
	public static final String READ = "READ";
	public static final String WRITE = "WRITE";

	public abstract String type();
}