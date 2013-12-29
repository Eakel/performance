package com.easyfun.eclipse.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.commons.io.IOUtils;

public class IOUtil {
	public static int copy(InputStream input, OutputStream output) throws IOException {
		return IOUtils.copy(input, output);
	}
	
	/** 使用默认编码*/
	public static void copy(InputStream input, Writer output)  throws IOException {
		IOUtils.copy(input, output);
	}
	
	/** 指定使用的编码方式*/
	public static void copy(InputStream input, Writer output, String encoding)  throws IOException {
		IOUtils.copy(input, output, encoding);
	}
}
