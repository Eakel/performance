package com.easyfun.eclipse.performance.trace.builder;

import java.io.IOException;
import java.io.Writer;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class MyXMLWriter extends XMLWriter {
	/** 是否不输出CDATA信息*/
	private boolean omitCDATA = false;

	public MyXMLWriter(Writer writer, OutputFormat format, boolean omitCDATA) {
		super(writer, format);
		this.omitCDATA = omitCDATA;
	}

	protected void writeCDATA(String text) throws IOException {
		if (omitCDATA) {
			writer.write(text);
			lastOutputNodeType = 4;
		} else {
			super.writeCDATA(text);
		}

	}
}
