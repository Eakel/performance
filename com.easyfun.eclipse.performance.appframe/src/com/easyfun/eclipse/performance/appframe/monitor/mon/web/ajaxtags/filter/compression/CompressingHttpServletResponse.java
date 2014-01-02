package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.filter.compression;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class CompressingHttpServletResponse extends HttpServletResponseWrapper {
	private static transient Log log = LogFactory.getLog(CompressingHttpServletResponse.class);
	private static final String ACCEPT_ENCODING_HEADER = "Accept-Encoding";
	private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
	private static final String CONTENT_LENGTH_HEADER = "Content-Length";
	private static final String CONTENT_TYPE_HEADER = "Content-Type";
	private static final String VARY_HEADER = "Vary";
	private static final String X_COMPRESSED_BY_HEADER = "X-Compressed-By";
	private static final String COMPRESSED_BY_VALUE = CompressingFilter.VERSION_STRING;
	private final HttpServletResponse httpResponse;
	private final CompressingFilterContext context;
	private final String compressedContentEncoding;
	private final CompressingStreamFactory compressingStreamFactory;
	private CompressingServletOutputStream compressingSOS;
	private PrintWriter printWriter;
	private boolean isGetOutputStreamCalled;
	private boolean isGetWriterCalled;
	private boolean compressing;
	private int savedContentLength;
	private boolean savedContentLengthSet;
	private String savedContentEncoding;
	private boolean contentTypeOK;

	CompressingHttpServletResponse(HttpServletResponse httpResponse, CompressingStreamFactory compressingStreamFactory, String contentEncoding,
			CompressingFilterContext context) {
		super(httpResponse);
		this.httpResponse = httpResponse;
		this.compressedContentEncoding = contentEncoding;
		this.compressing = false;
		this.compressingStreamFactory = compressingStreamFactory;
		this.context = context;
		this.contentTypeOK = true;
	}

	public ServletOutputStream getOutputStream() throws IOException {
		if (this.isGetWriterCalled) {
			throw new IllegalStateException("getWriter() 已经被调用");
		}
		this.isGetOutputStreamCalled = true;
		return getCompressingServletOutputStream();
	}

	public PrintWriter getWriter() throws IOException {
		if (this.isGetOutputStreamCalled) {
			throw new IllegalStateException("getCompressingOutputStream() 已经被调用");
		}
		this.isGetWriterCalled = true;
		if (this.printWriter == null) {
			this.printWriter = new PrintWriter(new OutputStreamWriter(getCompressingServletOutputStream(), getCharacterEncoding()), true);
		}

		return this.printWriter;
	}

	public void addHeader(String name, String value) {
		if (isAllowedHeader(name))
			this.httpResponse.addHeader(name, value);
	}

	public void addIntHeader(String name, int value) {
		if (isAllowedHeader(name))
			this.httpResponse.addIntHeader(name, value);
	}

	public void addDateHeader(String name, long value) {
		if (isAllowedHeader(name))
			this.httpResponse.addDateHeader(name, value);
	}

	public void setHeader(String name, String value) {
		if (CONTENT_ENCODING_HEADER.equalsIgnoreCase(name)) {
			this.savedContentEncoding = value;
		} else if (CONTENT_LENGTH_HEADER.equalsIgnoreCase(name)) {
			setContentLength(Integer.parseInt(value));
		} else if (CONTENT_TYPE_HEADER.equalsIgnoreCase(name)) {
			setContentType(value);
		} else if (isAllowedHeader(name))
			this.httpResponse.setHeader(name, value);
	}

	public void setIntHeader(String name, int value) {
		if (CONTENT_LENGTH_HEADER.equalsIgnoreCase(name)) {
			setContentLength(value);
		} else if (isAllowedHeader(name))
			this.httpResponse.setIntHeader(name, value);
	}

	public void setDateHeader(String name, long value) {
		if (isAllowedHeader(name))
			this.httpResponse.setDateHeader(name, value);
	}

	public void flushBuffer() throws IOException {
		flushWriter();
		if (this.compressingSOS != null)
			this.compressingSOS.flush();
	}

	public void reset() {
		flushWriter();
		if (this.compressingSOS != null) {
			this.compressingSOS.reset();
		}
		this.httpResponse.reset();
		if (this.compressing)
			setResponseHeaders();
	}

	public void resetBuffer() {
		flushWriter();
		if (this.compressingSOS != null) {
			this.compressingSOS.reset();
		}
		this.httpResponse.resetBuffer();
	}

	public void setContentLength(int contentLength) {
		if (this.compressing) {
			log.debug("因为响应已经被压缩，所以忽略");
		} else {
			this.savedContentLength = contentLength;
			this.savedContentLengthSet = true;
			log.debug("保存内容长度:" + contentLength);
		}
	}

	public void setContentType(String contentType) {
		this.contentTypeOK = isCompressableContentType(contentType);
		if (!this.compressing)
			this.httpResponse.setContentType(contentType);
	}

	public String toString() {
		return "CompressingHttpServletResponse[compressing: " + this.compressing + ']';
	}

	boolean isCompressing() {
		return this.compressing;
	}

	void close() throws IOException {
		if ((this.compressingSOS != null) && (!this.compressingSOS.isClosed()))
			this.compressingSOS.close();
	}

	private void setResponseHeaders() {
		log.debug("设置压缩相关的头");
		this.httpResponse.setHeader(CONTENT_ENCODING_HEADER, this.compressedContentEncoding);
		this.httpResponse.addHeader(VARY_HEADER, ACCEPT_ENCODING_HEADER);
		this.httpResponse.setHeader(X_COMPRESSED_BY_HEADER, COMPRESSED_BY_VALUE);
	}

	void rawStreamCommitted() {
		log.debug("无压缩的提交响应");
		if (this.savedContentLengthSet) {
			this.httpResponse.setContentLength(this.savedContentLength);
		}
		if (this.savedContentEncoding != null)
			this.httpResponse.setHeader(CONTENT_ENCODING_HEADER, this.savedContentEncoding);
	}

	void switchToCompression() {
		log.debug("在响应中使用压缩");
		this.compressing = true;
		setResponseHeaders();
	}

	private boolean isAllowedHeader(String header) {
		boolean result = (header == null)
				|| ((!CONTENT_LENGTH_HEADER.equalsIgnoreCase(header)) && (!CONTENT_ENCODING_HEADER.equalsIgnoreCase(header)) && (!X_COMPRESSED_BY_HEADER
						.equalsIgnoreCase(header)));

		if ((!result) && (log.isDebugEnabled())) {
			log.debug("头 '" + header + "' 不能被应用设置");
		}
		return result;
	}

	private void flushWriter() {
		if (this.printWriter != null)
			this.printWriter.flush();
	}

	private boolean isCompressableContentType(String contentType) {
		String contentTypeOnly = contentType;
		if (contentType != null) {
			int semicolonIndex = contentType.indexOf(';');
			if (semicolonIndex >= 0) {
				contentTypeOnly = contentType.substring(0, semicolonIndex);
			}
		}

		boolean isContained = this.context.getContentTypes().contains(contentTypeOnly);
		return !isContained ? true : this.context.isIncludeContentTypes() ? isContained : false;
	}

	private CompressingServletOutputStream getCompressingServletOutputStream() throws IOException {
		if (this.compressingSOS == null) {
			this.compressingSOS = new CompressingServletOutputStream(this.httpResponse.getOutputStream(), this.compressingStreamFactory, this, this.context);
		}

		if (mustNotCompress()) {
			this.compressingSOS.abortCompression();
		} else if (mustCompress()) {
			this.compressingSOS.engageCompression();
		}

		return this.compressingSOS;
	}

	private boolean mustNotCompress() {
		if (!this.contentTypeOK) {
			log.debug("Will not compress since configuration excludes this content type");
			return true;
		}
		if ((this.savedContentLengthSet) && (this.savedContentLength < this.context.getCompressionThreshold())) {
			log.debug("Will not compress since page has set a content length which is less than the compression threshold: " + this.savedContentLength);

			return true;
		}
		return false;
	}

	private boolean mustCompress() {
		if ((this.savedContentLengthSet) && (this.savedContentLength >= this.context.getCompressionThreshold())) {
			log.debug("Will begin compression immediately since page has set a content length which is is greater than or equal to the compression threshold: "
					+ this.savedContentLength);

			return true;
		}
		return false;
	}
}