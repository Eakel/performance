package com.easyfun.eclipse.common.util.resource;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import com.easyfun.eclipse.performance.PerformanceActivator;

public class ResourceUtils {
	public static File getBundleFile(String path){
		URL url=FileLocator.find(PerformanceActivator.getDefault().getBundle(), 
				new Path(path), null);
		File file=null;
		if(url!=null){
			try {
				URL fileUrl=FileLocator.toFileURL(url);
				file=new File(fileUrl.toURI());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

}
