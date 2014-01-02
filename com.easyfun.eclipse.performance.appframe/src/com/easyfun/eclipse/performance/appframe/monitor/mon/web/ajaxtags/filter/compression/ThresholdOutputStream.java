package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.filter.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class ThresholdOutputStream extends OutputStream {
	private boolean buffering;
	private final OutputStream out1;
	private OutputStream out2;
	private CompressingStream compressingStream;
	private final CompressingStreamFactory compressingStreamFactory;
	private final CompressingFilterContext context;
	private final int threshold;
	private final BufferCommitmentCallback bufferCommitmentCallback;
	private ByteArrayOutputStream buffer;
	private boolean closed;
	private boolean forceOut1;

	public ThresholdOutputStream(OutputStream out1, CompressingStreamFactory compressingStreamFactory, CompressingFilterContext context,
			BufferCommitmentCallback thresholdReachedCallback) {
		this.buffering = true;
		this.out1 = out1;
		this.compressingStreamFactory = compressingStreamFactory;
		this.context = context;
		this.threshold = context.getCompressionThreshold();
		this.bufferCommitmentCallback = thresholdReachedCallback;
	}

	public void write(int b) throws IOException {
		checkClosed();
		if (this.forceOut1) {
			this.out1.write(b);
		} else if (continueBuffering(1)) {
			this.buffer.write(b);
		} else
			this.out2.write(b);
	}

	public void write(byte[] b) throws IOException {
		checkClosed();
		if (this.forceOut1) {
			this.out1.write(b);
		} else if (continueBuffering(b.length)) {
			this.buffer.write(b);
		} else
			this.out2.write(b);
	}

	public void write(byte[] b, int offset, int length) throws IOException {
		checkClosed();
		if (this.forceOut1) {
			this.out1.write(b, offset, length);
		} else if (continueBuffering(length)) {
			this.buffer.write(b, offset, length);
		} else
			this.out2.write(b, offset, length);
	}

	public void flush() throws IOException {
		checkClosed();
		if (this.forceOut1) {
			this.out1.flush();
		} else if (!this.buffering)
			this.out2.flush();
	}

	public void close() throws IOException {
		checkClosed();
		this.closed = true;

		if (this.forceOut1) {
			this.out1.flush();
			this.out1.close();
		} else if (this.buffering) {
			forceOutputStream1();
			this.out1.flush();
			this.out1.close();
		} else {
			this.out2.flush();
			this.compressingStream.finish();
			this.out2.close();
		}
	}

	void reset() {
		if ((this.forceOut1) || (!this.buffering)) {
			throw new IllegalStateException("Can't reset");
		}
		if (this.buffer != null)
			this.buffer.reset();
	}

	public String toString() {
		return ThresholdOutputStream.class.getName();
	}

	private boolean continueBuffering(int numAdditionalBytes) throws IOException {
		boolean shouldContinue = false;
		if (this.buffering) {
			if (this.buffer == null) {
				if (numAdditionalBytes >= this.threshold) {
					switchToOutputStream2();
				} else {
					this.buffer = new ByteArrayOutputStream(this.threshold);
					shouldContinue = true;
				}
			} else if (this.buffer.size() + numAdditionalBytes >= this.threshold) {
				switchToOutputStream2();
			} else {
				shouldContinue = true;
			}
		}
		return shouldContinue;
	}

	void forceOutputStream1() throws IOException {
		this.forceOut1 = true;
		if (this.bufferCommitmentCallback != null) {
			this.bufferCommitmentCallback.rawStreamCommitted();
		}
		flushBufferToStream(this.out1);
	}

	void switchToOutputStream2() throws IOException {
		if (this.bufferCommitmentCallback != null) {
			this.bufferCommitmentCallback.compressingStreamCommitted();
		}
		this.compressingStream = this.compressingStreamFactory.getCompressingStream(this.out1, this.context);
		this.out2 = this.compressingStream.getCompressingOutputStream();
		flushBufferToStream(this.out2);
	}

	private void flushBufferToStream(OutputStream out) throws IOException {
		if (this.buffer != null) {
			this.buffer.writeTo(out);
			this.buffer = null;
		}
		this.buffering = false;
	}

	private void checkClosed() {
		if (this.closed) {
			throw new IllegalStateException("Stream is closed");
		}
	}

	static interface BufferCommitmentCallback {
		public void rawStreamCommitted();

		public void compressingStreamCommitted();
	}
}