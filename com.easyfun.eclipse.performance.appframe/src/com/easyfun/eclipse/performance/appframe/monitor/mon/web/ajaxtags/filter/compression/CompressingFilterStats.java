package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.filter.compression;

import java.io.Serializable;

public final class CompressingFilterStats implements Serializable {
	private static final long serialVersionUID = 7738198707610932737L;
	public static final String STATS_KEY = CompressingFilterStats.class.getName();
	private int numResponsesCompressed;
	private int totalResponsesNotCompressed;
	private long inputBytes;
	private long compressedBytes;
	private final StatsCallback inputStatsCallback;
	private final StatsCallback compressedStatsCallback;

	public CompressingFilterStats() {
		this.numResponsesCompressed = 0;
		this.totalResponsesNotCompressed = 0;
		this.inputBytes = 0L;
		this.compressedBytes = 0L;
		this.inputStatsCallback = new StatsCallback(true);
		this.compressedStatsCallback = new StatsCallback(false);
	}

	public int getNumResponsesCompressed() {
		return this.numResponsesCompressed;
	}

	public void incrementNumResponsesCompressed() {
		this.numResponsesCompressed += 1;
	}

	public int getTotalResponsesNotCompressed() {
		return this.totalResponsesNotCompressed;
	}

	public void incrementTotalResponsesNotCompressed() {
		this.totalResponsesNotCompressed += 1;
	}

	public long getInputBytes() {
		return this.inputBytes;
	}

	public long getCompressedBytes() {
		return this.compressedBytes;
	}

	public double getAverageCompressionRatio() {
		return this.compressedBytes == 0L ? 0.0D : this.inputBytes / this.compressedBytes;
	}

	public String toString() {
		return "压缩过滤器[响应被压缩的URL数量: " + this.numResponsesCompressed + ", 平均实际大小/压缩后的大小: " + getAverageCompressionRatio() + ']';
	}

	StatsCallback getInputStatsCallback() {
		return this.inputStatsCallback;
	}

	StatsCallback getCompressedStatsCallback() {
		return this.compressedStatsCallback;
	}

	final class StatsCallback implements StatsOutputStream.StatsCallback, Serializable {
		private static final long serialVersionUID = -4273878390334498750L;
		private final boolean input;

		StatsCallback(boolean input) {
			this.input = input;
		}

		public void bytesWritten(int numBytes) {
//			if (this.input) {
//				CompressingFilterStats.access$014(CompressingFilterStats.this, numBytes);
//			} else
//				CompressingFilterStats.access$114(CompressingFilterStats.this, numBytes);
		}

		public String toString() {
			return "StatsCallback[input: " + this.input + ']';
		}
	}
}