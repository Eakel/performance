package com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 仅仅定义了一些共用方法
 * 
 * @author linzm
 * 
 * Created at 2012-9-20
 */
public abstract class BaseAction {
	/** */
	public String getStringFormInputStreamString(HttpServletRequest request) throws IOException {
		StringBuffer rtn = new StringBuffer();
		ServletInputStream aReader = request.getInputStream();
		byte[] buf = new byte[8192];
		while (true) {
			int result = aReader.readLine(buf, 0, buf.length);
			if (result == -1) {
				break;
			}
			String line = new String(buf, 0, result, "UTF-8");
			rtn.append(line);
		}

		return rtn.toString();
	}

	/** 以GBK的形式读取参数*/
	protected String getParameter(HttpServletRequest request, String name) throws Exception {
		String rtn = new String(request.getParameter(name).getBytes("ISO-8859-1"), "GBK");
		return rtn;
	}

	/** 设置Response
	 * <li>"text/xml; charset=GBK"
	 * <li>"Cache-Control", "no-cache"
	 * */
	public void showInfo(HttpServletResponse response, String rtn) {
		try {
			response.setContentType("text/xml; charset=GBK");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(rtn);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/** 设置Response，指定encoding
	 * <li>"text/xml; charset=[encoding]
	 * <li>"Cache-Control", "no-cache"
	 * */
	public void showInfo(HttpServletResponse response, String rtn, String encoding) {
		try {
			response.setContentType("text/xml; charset=" + encoding + "");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(rtn);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/** 设置Response
	 * <li>"text/html; charset=GBK"
	 * <li>"Cache-Control", "no-cache"
	 * */
	public void showHtmlInfo(HttpServletResponse response, String rtn) {
		try {
			response.setContentType("text/html; charset=GBK");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(rtn);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		byte[] b = "asdsaasd阿瑟大撒".getBytes();
		System.out.println(new String(b, 0, b.length, "UTF-8"));
	}
}