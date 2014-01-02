package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.filter.compression;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;

public final class CompressingServletOutputStream extends ServletOutputStream {
	private final OutputStream rawStream;
	private final CompressingStreamFactory compressingStreamFactory;
	private final CompressingHttpServletResponse compressingResponse;
	private final CompressingFilterContext context;
	private ThresholdOutputStream thresholdOutputStream;
	private boolean closed;

	public CompressingServletOutputStream(OutputStream rawStream, CompressingStreamFactory compressingStreamFactory,
			CompressingHttpServletResponse compressingResponse, CompressingFilterContext context) {
		this.rawStream = rawStream;
		this.compressingStreamFactory = compressingStreamFactory;
		this.compressingResponse = compressingResponse;
		this.context = context;
		this.closed = false;
	}

	public void write(byte[] b) throws IOException {
		checkWriteState();
		this.thresholdOutputStream.write(b);
	}

	public void write(byte[] b, int offset, int length) throws IOException {
		checkWriteState();
		this.thresholdOutputStream.write(b, offset, length);
	}

	public void write(int b) throws IOException {
		checkWriteState();
		this.thresholdOutputStream.write(b);
	}

	public void flush() throws IOException {
		if ((!this.closed) && (this.thresholdOutputStream != null))
			this.thresholdOutputStream.flush();
	}

	public void close() throws IOException {
		if (!this.closed) {
			this.compressingResponse.flushBuffer();
			this.closed = true;
			if (this.thresholdOutputStream != null)
				this.thresholdOutputStream.close();
		}
	}

	public String toString() {
		return CompressingServletOutputStream.class.getName();
	}

	boolean isClosed() {
		return this.closed;
	}

	void reset() {
		checkClosed();

		if (this.thresholdOutputStream != null)
			this.thresholdOutputStream.reset();
	}

	void engageCompression() throws IOException {
		checkWriteState();
		this.thresholdOutputStream.switchToOutputStream2();
	}

	void abortCompression() throws IOException {
		checkWriteState();
		this.thresholdOutputStream.forceOutputStream1();
	}

	private void checkWriteState() {
		checkClosed();
		if (this.thresholdOutputStream == null)
			this.thresholdOutputStream = new ThresholdOutputStream(this.rawStream, this.compressingStreamFactory, this.context,
					new ResponseBufferCommitmentCallback(this.compressingResponse));
	}

	private void checkClosed() {
		if (this.closed)
			throw new IllegalStateException("流已经被关闭");
	}

	private static final class ResponseBufferCommitmentCallback implements ThresholdOutputStream.BufferCommitmentCallback {
		private final CompressingHttpServletResponse response;

		private ResponseBufferCommitmentCallback(CompressingHttpServletResponse response) {
			this.response = response;
		}

		public void rawStreamCommitted() {
			this.response.rawStreamCommitted();
		}

		public void compressingStreamCommitted() {
			this.response.switchToCompression();
		}

		public String toString() {
			return ResponseBufferCommitmentCallback.class.getName();
		}
	}
}