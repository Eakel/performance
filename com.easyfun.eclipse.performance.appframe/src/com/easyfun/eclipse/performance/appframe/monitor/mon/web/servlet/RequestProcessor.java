package com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonOpLog;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.TreeAction;

public class RequestProcessor {
	private static transient Log log = LogFactory.getLog(RequestProcessor.class);
	private static final String DEFAULT_VIEW = "view";
	public static final String ACTION = "action";
	protected Class[] types = { HttpServletRequest.class, HttpServletResponse.class };

	public void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String className = null;
		String methodName = null;
		try {
			className = getClassName(request);
			if ((StringUtils.isBlank(className)) || (StringUtils.isEmpty(className))) {
				throw new Exception("����Ϊ��!");
			}

			methodName = getMethodName(request);
			if ((StringUtils.isBlank(methodName)) || (StringUtils.isEmpty(methodName))) {
				throw new Exception("������Ϊ��!");
			}
			
			String callType = request.getParameter("call_type");
			if (log.isInfoEnabled() == true) {
				log.info("call_type:" + callType);
			}

			exeMethod(className, methodName, request, response, callType);
		} catch (Exception ex) {
			log.error("�����������ͷ������� ex:" + ex);
			throw ex;
		}
	}

	public void exeMethod(String pClassName, String pMethodName, HttpServletRequest request, HttpServletResponse response, String callType) throws Exception {
		try {
			if (log.isDebugEnabled() == true) {
				log.debug("��ʼ������:" + pMethodName);
			}
			Class cls = Class.forName(pClassName);
			Object clsObj = cls.getDeclaredConstructor(null).newInstance(null);
			Object[] args = { request, response };
			Method method = cls.getMethod(pMethodName, this.types);

			boolean isPass = false;
			String key = pClassName.trim() + "." + pMethodName.trim();

			HashMap data = (HashMap) request.getSession().getAttribute("ACCESSLIST");
			
			if(StringUtils.isNotEmpty(callType)){
				key = pClassName.trim() + "?call_type=" + callType + "." + pMethodName.trim();
				if(log.isInfoEnabled()){
					log.info("Access Key is:" + key);
				}
				if ((data != null) && (data.containsKey(key))) {
					isPass = true;
				}
			}else{
				if ((data != null) && (data.containsKey(key))) {
					if(log.isInfoEnabled()){
						log.info("Access Key is:" + key);
					}
					isPass = true;
				}
			}


			if (pClassName.trim().equalsIgnoreCase(TreeAction.class.getName())) {
				isPass = true;
			}

			if (isPass) {
				HttpSession s = request.getSession(false);
				String username = null;
				if (s != null) {
					username = (String) s.getAttribute("USERNAME");
				}

				if (!pClassName.trim().equalsIgnoreCase(TreeAction.class.getName())) {
					String ip = getIpAddr(request);

					String url = StringUtils.substringAfter(request.getRequestURI().toString(), request.getContextPath());
					String query = request.getQueryString();
					if (!StringUtils.isBlank(query)) {
						url = url + "?" + query;
					}

					Enumeration enu = request.getParameterNames();
					while (enu.hasMoreElements()) {
						String name = (String) enu.nextElement();
						String para = request.getParameter(name);
						url = url + "&" + name + "=" + para;
					}

					if(StringUtils.isNotEmpty(callType)){
						log.error("�û�:" + username + ",IP:" + ip + ",������:" + pClassName.trim() + "?call_type=" + callType + ",����:" + pMethodName + ",URL:" + url);
					}else{
						log.error("�û�:" + username + ",IP:" + ip + ",������:" + pClassName.trim() + ",����:" + pMethodName + ",URL:" + url);
					}

					MonOpLog objMonOpLog = new MonOpLog();
					objMonOpLog.setCode(username);
					objMonOpLog.setIp(ip);
					if(StringUtils.isNotEmpty(callType)){
						objMonOpLog.setClassName(pClassName.trim() + "?call_type=" + callType);
					}else{
						objMonOpLog.setClassName(pClassName);
					}
					objMonOpLog.setMethodName(pMethodName);
					objMonOpLog.setUrl(splitByLength(url, 1998)[0]);

					IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
					objIMonSV.insertMonOpLog(objMonOpLog);
				}

				method.invoke(clsObj, args);

				if (log.isDebugEnabled() == true)
					log.debug("����������:" + pMethodName);
			} else {
				response.setContentType("text/xml; charset=GBK");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write("�޴˲���Ȩ��,key=" + key);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	private String getClassName(HttpServletRequest request) {
		try {
			String requestUrl = request.getPathInfo();
			if ((StringUtils.isEmpty(requestUrl)) || (StringUtils.isBlank(requestUrl))) {
				throw new Exception("���������û���������������ļ���key");
			}

			String[] detailUrl = StringUtils.split(requestUrl, '/');

			if (detailUrl.length == 1) {
				detailUrl = new String[] { "view", detailUrl[0] };
			}

			if (detailUrl.length < 2) {
				throw new Exception("û���ҵ�ģ�������Ӧ�÷�������淶:http://localhost:8080/business/so/test?action=save");
			}

			String moduleName = detailUrl[0];
			String key = detailUrl[1];

			if ((StringUtils.isEmpty(key)) || (StringUtils.isBlank(key))) {
				throw new Exception("���������û���������������ļ���key");
			}

			String className = null;

			if (log.isDebugEnabled() == true) {
				log.debug("ʹ����ģʽ");
			}
			className = key;

			if (log.isDebugEnabled() == true) {
				log.debug("����:" + className);
			}

			return className;
		} catch (Exception ex) {
		}
		return null;
	}

	private String getMethodName(HttpServletRequest request) throws Exception {
		String methodName = request.getParameter("action");
		if (log.isDebugEnabled() == true) {
			log.debug("������:" + methodName);
		}

		if ((StringUtils.isEmpty(methodName) == true) || (StringUtils.isBlank(methodName) == true)) {
			throw new Exception("���������û��action�����������action��Ӧ���������Ϊ��");
		}
		return methodName;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = null;
		Enumeration enu = request.getHeaderNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			if (name.equalsIgnoreCase("X-Forwarded-For")) {
				ip = request.getHeader(name);
			} else if (name.equalsIgnoreCase("Proxy-Client-IP")) {
				ip = request.getHeader(name);
			} else if (name.equalsIgnoreCase("WL-Proxy-Client-IP")) {
				ip = request.getHeader(name);
			}

			if ((ip != null) && (ip.length() != 0)) {
				break;
			}
		}
		if ((ip == null) || (ip.length() == 0)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String[] splitByLength(String xml, int length) {
		String[] rtn = null;

		int count = xml.length() / length + 1;
		rtn = new String[count];
		for (int i = 0; i < count; i++) {
			rtn[i] = StringUtils.substring(xml, length * i, length * (i + 1));
		}
		return rtn;
	}

	public static void main(String[] args) {
		String str = "/so/test?save=sds";
		String[] ss = StringUtils.split(str, '/');
		for (int i = 0; i < ss.length; i++)
			System.out.println(ss[i]);
	}
}