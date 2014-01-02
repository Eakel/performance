package com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonAccess;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonUser;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.webtool.util.appf.K;

/**
 * 登录的Servlet，配置在web.xml中
 * <li>/loginservlet
 * @author linzhaoming
 * 
 * Created at 2012-9-16
 */
public class LoginServlet extends HttpServlet {

	private static String REGEX = "(?=^.{6,25}$)(?=(?:.*?\\d){1})(?=.*[a-z])(?=(?:.*?[A-Z]){1})(?=(?:.*?[!@#$%*()_\\-+^&}{:;?.]){1})(?!.*\\s)[0-9a-zA-Z!@#$%*()_\\-+^&]*$";

	private static Pattern PATTERN = Pattern.compile(REGEX);
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			process(req, resp);
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String modifyPassword = req.getParameter("modifyPassword");

			if (!StringUtils.isBlank(modifyPassword)) {
				modifyPassword(req, resp);
			} else {
				process(req, resp);
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	protected void modifyPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String oldPwd = request.getParameter("oldPwd");
			String newPwd1 = request.getParameter("newPwd1");
			String newPwd2 = request.getParameter("newPwd2");

			if (StringUtils.isBlank(oldPwd)) {
				showInfo(response, "旧密码不能为空");
				return;
			}

			if (StringUtils.isBlank(newPwd1)) {
				showInfo(response, "新密码不能为空");
				return;
			}

			if (StringUtils.isBlank(newPwd2)) {
				showInfo(response, "确认新密码不能为空");
				return;
			}

			if (!newPwd1.equals(newPwd2)) {
				showInfo(response, "新密码和确认新密码必须一致");
				return;
			}

			Matcher m = PATTERN.matcher(newPwd1);
			System.out.println(m.matches());
			if (!m.matches()) {
				showInfo(response, "新密码必须至少6位,包含至少1个特殊字符,1个数字,1个大写字母和一些小写字母");
				return;
			}

			String username = (String) request.getSession().getAttribute("USERNAME");
			if (StringUtils.isBlank(username)) {
				showInfo(response, "必须登陆后才能修改密码");
				return;
			}

			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
			MonUser user = objIMonSV.getMonUser(username.trim());
			if (!K.k_s(user.getPassword()).equalsIgnoreCase(oldPwd)) {
				showInfo(response, "旧密码不正确");
				return;
			}

			objIMonSV.updatePwdByUsername(username.trim(), newPwd1);
			showInfo(response, "Y");
		} catch (Exception ex) {
			ex.printStackTrace();
			showInfo(response, "N");
		}
	}

	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			if ((!StringUtils.isBlank(request.getParameter("username"))) && (!StringUtils.isBlank(request.getParameter("password")))) {
				IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
				MonUser objMonUser = objIMonSV.getMonUser(request.getParameter("username"));

				String pwd = request.getParameter("password");
				if (K.k_s(objMonUser.getPassword()).equalsIgnoreCase(pwd)) {
					String username = request.getParameter("username").trim();
					request.getSession().setAttribute("USERNAME", username);

					MonAccess[] accessList = objIMonSV.getMonAccessByUsername(username);
					HashMap data = new HashMap();
					if ((accessList != null) && (accessList.length > 0)) {
						for (int i = 0; i < accessList.length; i++) {
							data.put(accessList[i].getClassName() + "." + accessList[i].getMethodName(), new Long(accessList[i].getAccessId()));
						}
					}
					request.getSession().setAttribute("ACCESSLIST", data);

					request.getRequestDispatcher("/main.jsp").forward(request, response);
				} else {
					request.getRequestDispatcher("/index.jsp?error=2").forward(request, response);
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/index.jsp?error=2");
		}
	}

	public void showInfo(HttpServletResponse response, String rtn) {
		try {
			response.setContentType("text/xml; charset=GBK");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(rtn);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}