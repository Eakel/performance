package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.filter.compression;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class CompressingFilterContext {
	private static transient Log log = LogFactory.getLog(CompressingFilterContext.class);
	private static final int DEFAULT_COMPRESSION_THRESHOLD = 1024;
	private final int compressionThreshold;
	private final ServletContext servletContext;
	private final CompressingFilterStats stats;
	private final boolean includeContentTypes;
	private final Set contentTypes;

	public CompressingFilterContext(FilterConfig filterConfig) throws ServletException {
		log.debug("Debug logging statements are enabled");

		this.compressionThreshold = readCompressionThresholdValue(filterConfig);
		if (log.isDebugEnabled()) {
			log.debug("压缩最小阀值: " + this.compressionThreshold);
		}

		this.servletContext = filterConfig.getServletContext();

		if (readStatsEnabledValue(filterConfig)) {
			this.stats = new CompressingFilterStats();
			ensureStatsInContext();
			log.debug("过滤器记录仪打开");
		} else {
			this.stats = null;
			log.debug("过滤器记录仪未打开");
		}

		String includeContentTypesString = filterConfig.getInitParameter("includeContentTypes");
		String excludeContentTypesString = filterConfig.getInitParameter("excludeContentTypes");
		if ((includeContentTypesString != null) && (excludeContentTypesString != null)) {
			throw new IllegalArgumentException("Can't specify both includeContentTypes and excludeContentTypes");
		}

		if (includeContentTypesString == null) {
			this.includeContentTypes = false;
			this.contentTypes = parseContentTypes(excludeContentTypesString);
		} else {
			this.includeContentTypes = true;
			this.contentTypes = parseContentTypes(includeContentTypesString);
		}

		log.debug("Filter will " + (this.includeContentTypes ? "include" : "exclude") + " only these content types: " + this.contentTypes);
	}

	int getCompressionThreshold() {
		return this.compressionThreshold;
	}

	CompressingFilterStats getStats() {
		ensureStatsInContext();
		return this.stats;
	}

	boolean isStatsEnabled() {
		return this.stats != null;
	}

	boolean isIncludeContentTypes() {
		return this.includeContentTypes;
	}

	public Set getContentTypes() {
		return this.contentTypes;
	}

	public String toString() {
		return CompressingFilterContext.class.getName();
	}

	private void ensureStatsInContext() {
		if (this.servletContext.getAttribute(CompressingFilterStats.STATS_KEY) == null)
			this.servletContext.setAttribute(CompressingFilterStats.STATS_KEY, this.stats);
	}

	private static boolean readStatsEnabledValue(FilterConfig filterConfig) {
		return readBooleanValue(filterConfig, "statsEnabled");
	}

	private static boolean readBooleanValue(FilterConfig filterConfig, String parameter) {
		return Boolean.valueOf(filterConfig.getInitParameter(parameter)).booleanValue();
	}

	private static int readCompressionThresholdValue(FilterConfig filterConfig) throws ServletException {
		String compressionThresholdString = filterConfig.getInitParameter("compressionThreshold");
		int value;
		if (compressionThresholdString != null) {
			try {
				value = Integer.parseInt(compressionThresholdString);
			} catch (NumberFormatException nfe) {
				throw new ServletException("Invalid compression threshold: " + compressionThresholdString, nfe);
			}
			if (value < 0)
				throw new ServletException("Compression threshold cannot be negative");
		} else {
			value = DEFAULT_COMPRESSION_THRESHOLD;
		}
		return value;
	}

	private static Set parseContentTypes(String contentTypesString) {
		if (contentTypesString == null) {
			return Collections.EMPTY_SET;
		}

		StringTokenizer st = new StringTokenizer(contentTypesString, ",");
		Set contentTypes = new HashSet(5);
		while (st.hasMoreTokens()) {
			String contentType = st.nextToken().trim();
			if (contentType.length() > 0) {
				contentTypes.add(contentType);
			}
		}
		return Collections.unmodifiableSet(contentTypes);
	}
}