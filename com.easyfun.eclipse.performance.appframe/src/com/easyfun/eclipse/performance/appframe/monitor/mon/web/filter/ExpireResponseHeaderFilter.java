package com.easyfun.eclipse.performance.appframe.monitor.mon.web.filter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

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

public class ExpireResponseHeaderFilter implements Filter {
	private static transient Log log = LogFactory.getLog(ExpireResponseHeaderFilter.class);

	private static String CONTEXT_PATH = null;
	private Pattern[] urlPattern = null;
	private String[] paraValue = null;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		if ((this.urlPattern != null) && (this.urlPattern.length != 0)) {
			try {
				HttpServletRequest request = (HttpServletRequest) req;
				String requestURI = request.getRequestURI();

				if (CONTEXT_PATH == null) {
					CONTEXT_PATH = request.getContextPath();
				}

				for (int i = 0; i < this.urlPattern.length; i++) {
					if (this.urlPattern[i].matcher(
							StringUtils.substring(requestURI, CONTEXT_PATH.length())).matches()) {
						if (this.paraValue[i].indexOf("A") != -1) {
							Calendar cal = new GregorianCalendar();
							cal.setTimeInMillis(System.currentTimeMillis());
							cal.add(13, Integer.parseInt(this.paraValue[i].substring(1)));
							response.addHeader("Cache-Control", "Private");
							response.addDateHeader("Expires", cal.getTimeInMillis());

							if (log.isDebugEnabled()) {
								log.debug("在" + requestURI + "的响应头加上 Cache-Control ,Private     Expires , " + cal.getTimeInMillis() + "标志");
							}
							break;
						}
						if (this.paraValue[i].indexOf("M") == -1)
							break;
						Calendar cal = new GregorianCalendar();
						File f = getRequestURLAbsoluteFile(request);
						if (f != null) {
							cal.setTimeInMillis(f.lastModified());
						} else {
							cal.setTimeInMillis(System.currentTimeMillis());
						}
						cal.add(13, Integer.parseInt(this.paraValue[i].substring(1)));
						response.addHeader("Cache-Control", "Private");
						response.addDateHeader("Expires", cal.getTimeInMillis());

						if (log.isDebugEnabled()) {
							log.debug("在" + requestURI + "的响应头加上 Cache-Control ,Private     Expires , " + cal.getTimeInMillis() + "标志");
						}
						break;
					}
				}

			} catch (Exception ex) {
				log.error("错误:", ex);
			}
		}

		chain.doFilter(req, response);
	}

	public void init(FilterConfig filterConfig) {
		List paraNameList = new ArrayList();
		List paraValueList = new ArrayList();

		Enumeration enu = filterConfig.getInitParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			paraNameList.add(name);
			paraValueList.add(filterConfig.getInitParameter(name));
			log.debug(name + ":" + filterConfig.getInitParameter(name));
		}

		this.urlPattern = tranformUrl2RegExp((String[]) (String[]) paraNameList.toArray(new String[0]));
		this.paraValue = ((String[]) (String[]) paraValueList.toArray(new String[0]));

		log.debug("生效响应过滤器初始化完成");
	}

	public void destroy() {
		this.urlPattern = null;
		this.paraValue = null;
		log.debug("生效响应过滤器销毁完成");
	}

	private Pattern[] tranformUrl2RegExp(String[] str) {
		Pattern[] p = new Pattern[str.length];
		for (int i = 0; i < str.length; i++) {
			String tmp = StringUtils.replace(str[i], ".", "\\.");
			tmp = StringUtils.replace(tmp, "*", ".*");
			p[i] = Pattern.compile(tmp);
		}
		return p;
	}

	private File getRequestURLAbsoluteFile(HttpServletRequest request) {
		File rtn = null;
		try {
			rtn = new File(request.getSession().getServletContext().getRealPath(request.getServletPath()));
		} catch (Exception ex) {
			log.error("错误:", ex);
		}
		return rtn;
	}
}