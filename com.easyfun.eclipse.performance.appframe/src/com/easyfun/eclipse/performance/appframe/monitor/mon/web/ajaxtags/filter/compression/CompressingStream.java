package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.filter.compression;

import java.io.IOException;
import java.io.OutputStream;

public interface CompressingStream {
	public OutputStream getCompressingOutputStream();

	public void finish() throws IOException;
}