package com.easyfun.eclipse.performance.appframe.monitor.mon.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoginFilter implements Filter {
	private static transient Log log = LogFactory.getLog(LoginFilter.class);
	public static final String USERNAME = "USERNAME";

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		try {
			String url = StringUtils.substringAfter(req.getRequestURI().toString(), req.getContextPath());
			if ((url.equals("/index.jsp")) || (url.equals("/loginservlet"))) {
				chain.doFilter(request, response);
			} else {
				String username = getUserName(req);
				if (StringUtils.isBlank(username)) {
					req.getRequestDispatcher("/index.jsp").forward(request, response);
				} else {
					chain.doFilter(request, response);
				}
			}

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	public void destroy() {
	}

	private String getUserName(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(USERNAME);
	}
}