package com.easyfun.eclipse.utils.lang;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.commons.io.IOUtils;

public class IOUtil {
	public static int copy(InputStream input, OutputStream output) throws IOException {
		return IOUtils.copy(input, output);
	}
	
	public static void copy(InputStream input, Writer output)  throws IOException {
		IOUtils.copy(input, output);
	}
}
