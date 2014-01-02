package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.filter.compression;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class CompressingFilter implements Filter {
	private static transient Log log = LogFactory.getLog(CompressingFilter.class);
	private static final String ALREADY_APPLIED_KEY = "org.yanghua.filter.compression.AlreadyApplied";
	public static final String FORCE_ENCODING_KEY = "org.yanghua.filter.compression.ForceEncoding";
	public static final String COMPRESSED_KEY = "org.yanghua.filter.compression.Compressed";
	public static final String VERSION = "3.1.0";
	public static final String VERSION_STRING = CompressingFilter.class.getName() + '/' + "3.1.0";

	private CompressingFilterContext context = null;

	public void init(FilterConfig config) throws ServletException {
		this.context = new CompressingFilterContext(config);
		log.debug("CompressingFilter��ʼ��");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if ((response.isCommitted()) || (request.getAttribute(ALREADY_APPLIED_KEY) != null)) {
			log.debug("��Ӧ�Ѿ����ύ���߹������Ѿ���Ӧ����");
			handleDoFilter(request, response, chain, false);
			return;
		}

		if ((!(request instanceof HttpServletRequest)) || (!(response instanceof HttpServletResponse))) {
			log.debug("����ѹ����http��request��response��������һ��������");
			handleDoFilter(request, response, chain, false);
			return;
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (log.isDebugEnabled()) {
			log.debug("��������: '" + httpRequest.getRequestURI() + '\'');
		}

		String contentEncoding = CompressingStreamFactory.getBestContentEncoding(httpRequest);

		if ("identity".equals(contentEncoding)) {
			log.debug("ѹ����������֧�ֻ��߱�����ܾ���������һ��������");
			handleDoFilter(request, response, chain, false);
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("ѹ����֧��; ʹ�õı����� '" + contentEncoding + '\'');
		}

		CompressingStreamFactory compressingStreamFactory = CompressingStreamFactory.getFactoryForContentEncoding(contentEncoding);

		CompressingHttpServletResponse compressingResponse = new CompressingHttpServletResponse(httpResponse, compressingStreamFactory, contentEncoding,
				this.context);

		if (log.isDebugEnabled()) {
			log.debug("��������Ĺ�����...");
		}
		handleDoFilter(request, compressingResponse, chain, true);
	}

	private void handleDoFilter(ServletRequest request, ServletResponse response, FilterChain chain, boolean attemptingToCompress) throws IOException,
			ServletException {
		request.setAttribute(ALREADY_APPLIED_KEY, Boolean.TRUE);

		chain.doFilter(request, response);

		if (attemptingToCompress) {
			CompressingHttpServletResponse compressingResponse = (CompressingHttpServletResponse) response;

			log.debug("�ر���Ӧ(���û�б��ر�)...");
			try {
				compressingResponse.close();
			} catch (IOException ioe) {
				log.error("ˢ�»������", ioe);
			}

			if (compressingResponse.isCompressing()) {
				request.setAttribute("org.yanghua.filter.compression.Compressed", Boolean.TRUE);
			}

			if (this.context.isStatsEnabled()) {
				this.context.getStats().incrementNumResponsesCompressed();
			}

		} else if (this.context.isStatsEnabled()) {
			this.context.getStats().incrementTotalResponsesNotCompressed();
		}
	}

	public void destroy() {
		log.debug("ѹ��������������...");
	}

	public String toString() {
		return VERSION_STRING;
	}
}