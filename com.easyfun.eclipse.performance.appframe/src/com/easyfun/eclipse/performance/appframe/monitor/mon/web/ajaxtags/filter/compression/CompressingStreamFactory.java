package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.filter.compression;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;

public abstract class CompressingStreamFactory {
	private static final CompressingStreamFactory GZIP_CSF = new CompressingStreamFactory() {
		CompressingStream getCompressingStream(final OutputStream outputStream, final CompressingFilterContext context) throws IOException {
			return new CompressingStream() {
				private final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(CompressingStreamFactory.getStatsOutputStream(outputStream, context,
						false));

				private final OutputStream statsOutputStream = CompressingStreamFactory.getStatsOutputStream(this.gzipOutputStream, context, true);

				public OutputStream getCompressingOutputStream() {
					return this.statsOutputStream;
				}

				public void finish() throws IOException {
					this.gzipOutputStream.finish();
				}
			};
		}
	};

	private static final CompressingStreamFactory ZIP_CSF = new CompressingStreamFactory() {
		CompressingStream getCompressingStream(final OutputStream outputStream, final CompressingFilterContext context) {
			return new CompressingStream() {
				private final ZipOutputStream zipOutputStream = new ZipOutputStream(CompressingStreamFactory.getStatsOutputStream(outputStream, context, false));

				private final OutputStream statsOutputStream = CompressingStreamFactory.getStatsOutputStream(this.zipOutputStream, context, true);

				public OutputStream getCompressingOutputStream() {
					return this.statsOutputStream;
				}

				public void finish() throws IOException {
					this.zipOutputStream.finish();
				}
			};
		}
	};

	private static final CompressingStreamFactory DEFLATE_CSF = new CompressingStreamFactory() {
		CompressingStream getCompressingStream(final OutputStream outputStream, final CompressingFilterContext context) {
			return new CompressingStream() {
				private final DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(CompressingStreamFactory.getStatsOutputStream(outputStream,
						context, false));

				private final OutputStream statsOutputStream = CompressingStreamFactory.getStatsOutputStream(this.deflaterOutputStream, context, true);

				public OutputStream getCompressingOutputStream() {
					return this.statsOutputStream;
				}

				public void finish() throws IOException {
					this.deflaterOutputStream.finish();
				}
			};
		}
	};
	static final String NO_ENCODING = "identity";
	private static final String ANY_ENCODING = "*";
	private static final String[] preferredEncodings = { "gzip", "deflate", "compress", "x-gzip", "x-compress", "identity" };

	private static final Map bestEncodingCache = Collections.synchronizedMap(new HashMap(23));
	private static final Map factoryMap;

	abstract CompressingStream getCompressingStream(OutputStream paramOutputStream, CompressingFilterContext paramCompressingFilterContext) throws IOException;

	private static OutputStream getStatsOutputStream(OutputStream outputStream, CompressingFilterContext context, boolean input) {
		OutputStream result;
		if (context.isStatsEnabled()) {
			CompressingFilterStats stats = context.getStats();
			CompressingFilterStats.StatsCallback callback = input ? stats.getInputStatsCallback() : stats.getCompressedStatsCallback();

			result = new StatsOutputStream(outputStream, callback);
		} else {
			result = outputStream;
		}
		return result;
	}

	private static boolean isSupportedContentEncoding(String contentEncoding) {
		return ("identity".equals(contentEncoding)) || (factoryMap.containsKey(contentEncoding));
	}

	static CompressingStreamFactory getFactoryForContentEncoding(String contentEncoding) {
		return (CompressingStreamFactory) factoryMap.get(contentEncoding);
	}

	static String getBestContentEncoding(HttpServletRequest httpRequest) {
		String forcedEncoding = (String) httpRequest.getAttribute("org.yanghua.filter.compression.ForceEncoding");
		String bestEncoding;
		if (forcedEncoding != null) {
			bestEncoding = forcedEncoding;
		} else {
			String acceptEncodingHeader = httpRequest.getHeader("Accept-Encoding");
			if (acceptEncodingHeader == null) {
				bestEncoding = "identity";
			} else {
				bestEncoding = (String) bestEncodingCache.get(acceptEncodingHeader);
				if (bestEncoding == null) {
					if (acceptEncodingHeader.indexOf(',') >= 0) {
						bestEncoding = selectBestEncoding(acceptEncodingHeader);
					} else {
						bestEncoding = parseBestEncoding(acceptEncodingHeader);
					}
					bestEncodingCache.put(acceptEncodingHeader, bestEncoding);
				}
			}
		}

		if (!isSupportedContentEncoding(bestEncoding)) {
			bestEncoding = "identity";
		}

		return bestEncoding;
	}

	private static String parseBestEncoding(String acceptEncodingHeader) {
		ContentEncodingQ contentEncodingQ = parseContentEncodingQ(acceptEncodingHeader);
		String contentEncoding = contentEncodingQ.getContentEncoding();
		String bestEncoding;
		if (contentEncodingQ.getQ() > 0.0D) {
			if (ANY_ENCODING.equals(contentEncoding)) {
				bestEncoding = preferredEncodings[0];
			} else {
				bestEncoding = contentEncoding;
			}

		} else {
			bestEncoding = "identity";
		}
		return bestEncoding;
	}

	private static String selectBestEncoding(String acceptEncodingHeader) {
		String bestEncoding = null;

		double bestQ = 0.0D;

		Set unacceptableEncodings = null;
		boolean willAcceptAnything = false;

		StringTokenizer st = new StringTokenizer(acceptEncodingHeader, ",");
		while (st.hasMoreTokens()) {
			ContentEncodingQ contentEncodingQ = parseContentEncodingQ(st.nextToken());
			String contentEncoding = contentEncodingQ.getContentEncoding();
			double q = contentEncodingQ.getQ();
			if (ANY_ENCODING.equals(contentEncoding)) {
				willAcceptAnything = q > 0.0D;
			} else if (q > 0.0D) {
				if (q > bestQ) {
					bestQ = q;
					bestEncoding = contentEncoding;
				}
			} else {
				if (unacceptableEncodings == null) {
					unacceptableEncodings = new HashSet(3);
				}
				unacceptableEncodings.add(contentEncoding);
			}

		}

		if (bestEncoding == null) {
			if (willAcceptAnything) {
				if ((unacceptableEncodings == null) || (unacceptableEncodings.isEmpty())) {
					bestEncoding = preferredEncodings[0];
				} else {
					for (int i = 0; i < preferredEncodings.length; i++) {
						if (!unacceptableEncodings.contains(preferredEncodings[i])) {
							bestEncoding = preferredEncodings[i];
							break;
						}
					}
					if (bestEncoding == null) {
						bestEncoding = "identity";
					}
				}
			} else {
				bestEncoding = "identity";
			}
		}

		return bestEncoding;
	}

	private static ContentEncodingQ parseContentEncodingQ(String contentEncodingString) {
		double q = 1.0D;

		int qvalueStartIndex = contentEncodingString.indexOf(';');
		String contentEncoding;
		if (qvalueStartIndex >= 0) {
			contentEncoding = contentEncodingString.substring(0, qvalueStartIndex).trim();
			String qvalueString = contentEncodingString.substring(qvalueStartIndex + 1).trim();
			if (qvalueString.startsWith("q=")) {
				try {
					q = Double.parseDouble(qvalueString.substring(2));
				} catch (NumberFormatException nfe) {
				}
			}
		} else {
			contentEncoding = contentEncodingString.trim();
		}

		return new ContentEncodingQ(contentEncoding, q);
	}

	static {
		Map temp = new HashMap(11);
		temp.put("gzip", GZIP_CSF);
		temp.put("x-gzip", GZIP_CSF);
		temp.put("compress", ZIP_CSF);
		temp.put("x-compress", ZIP_CSF);
		temp.put("deflate", DEFLATE_CSF);
		factoryMap = Collections.unmodifiableMap(temp);
	}

	private static final class ContentEncodingQ {
		private final String contentEncoding;
		private final double q;

		private ContentEncodingQ(String contentEncoding, double q) {
			this.contentEncoding = contentEncoding;
			this.q = q;
		}

		private String getContentEncoding() {
			return this.contentEncoding;
		}

		private double getQ() {
			return this.q;
		}

		public String toString() {
			return this.contentEncoding + ";q=" + this.q;
		}
	}
}