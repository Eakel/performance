package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.filter.compression;

import java.io.IOException;
import java.io.OutputStream;

public final class StatsOutputStream extends OutputStream {
	private final OutputStream outputStream;
	private final StatsCallback statsCallback;

	StatsOutputStream(OutputStream outputStream, StatsCallback statsCallback) {
		this.outputStream = outputStream;
		this.statsCallback = statsCallback;
	}

	public void write(int b) throws IOException {
		this.outputStream.write(b);
		this.statsCallback.bytesWritten(1);
	}

	public void write(byte[] b) throws IOException {
		this.outputStream.write(b);
		if ((b != null) && (b.length > 0))
			this.statsCallback.bytesWritten(b.length);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		this.outputStream.write(b, off, len);
		if (len > 0) {
			this.statsCallback.bytesWritten(len);
		}
	}

	public void flush() throws IOException {
		this.outputStream.flush();
	}

	public void close() throws IOException {
		this.outputStream.close();
	}

	public String toString() {
		return "StatsOutputStream[" + this.outputStream + ']';
	}

	static interface StatsCallback {
		public void bytesWritten(int paramInt);
	}
}