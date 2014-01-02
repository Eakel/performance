package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asiainfo.mon.util.MD5;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.session.SessionManager;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class HotPatchAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(HotPatchAction.class);

	public void setValue2Session(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String hpPassword = request.getParameter("hpPassword");
			if (StringUtils.isBlank(hpPassword)) {
				super.showInfo(response, "��������HotPatch����");
				log.error("��������HotPatch����");
				return;
			}
			
			String callType = request.getParameter("call_type");
			if (StringUtils.isBlank(callType)) {
				log.error("call_typeΪ��");
				return;
			}

//			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
//			boolean rtn = objIMonSV.checkHotPatchPassword(hpPassword);
			
			boolean rtn = false;
			Connection conn = null;
			PreparedStatement ptmt = null;
			ResultSet rs = null;
			try {
				long count = 0L;
				conn = SessionManager.getSession().getConnection();
				ptmt = conn.prepareStatement("select count(*) from mon_hotpatch_password where  password = ? and state='U' and lower(call_type)=?");
				MD5 md5 = new MD5();
				String encrypt = md5.getMD5ofStr(hpPassword);
				ptmt.setString(1, encrypt);
				ptmt.setString(2, callType.trim().toLowerCase());
				rs = ptmt.executeQuery();
				if (rs.next()) {
					count = rs.getLong(1);
				}

				if (count > 0L) {
					rtn = true;
				}
			} catch (Exception ex) {
				throw ex;
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
			
			if (!rtn) {
				super.showInfo(response, "HotPatch���벻��ȷ");
				log.error("HotPatch���벻��ȷ");
				return;
			}

			String tmp1 = request.getParameter("server_ids");
			request.getSession().setAttribute("hotpatch.server_ids", tmp1);
			String tmp2 = request.getParameter("serverList");
			request.getSession().setAttribute("hotpatch.serverList", tmp2);
			super.showInfo(response, "1");
		} catch (Exception ex) {
			log.error("ex", ex);
			super.showInfo(response, ex.getMessage());
		}
	}

	@ActionPermission(type = "READ")
	public void showHotPatchList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tmp = request.getParameter("server_ids");
		String[] ids = StringUtils.split(tmp, ",");
		StringBuffer all = new StringBuffer();

		long[] tmpIds = new long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			tmpIds[i] = Long.parseLong(ids[i]);
		}

		List rtn = ParallelUtil.getHotPatchMap(20, 8, tmpIds);
		for (Iterator iter = rtn.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("<data>");
				sb.append("<SERVER_NAME>" + (String) map.get("SERVER_NAME") + "</SERVER_NAME>");
				String[] tmp2 = (String[]) (String[]) map.get("HOTPATCH_LIST");
				if ((tmp2 == null) || (tmp2.length == 0)) {
					sb.append("<HOTPATCH_LIST>HOTPATCH_LISTΪ��</HOTPATCH_LIST>");
				} else {
					sb.append("<HOTPATCH_LIST>" + StringUtils.join(tmp2, ";\r\n") + "</HOTPATCH_LIST>");
				}
				sb.append("</data>");

				all.append(sb.toString());
			} catch (Throwable ex) {
				log.error("�쳣", ex);
			}
		}

		if (all.length() != 0)
			showInfo(response, all.toString(), "UTF-8");
	}

	@ActionPermission(type = "WRITE")
	public void doHotPatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean isScript = false;
		String strIsScript = request.getParameter("isScript");
		if ((!StringUtils.isBlank(strIsScript)) && (strIsScript.trim().equalsIgnoreCase("1"))) {
			isScript = true;
		}

		String tmp = (String) request.getSession().getAttribute("hotpatch.server_ids");
		String[] ids = StringUtils.split(tmp, ",");

		long[] tmpIds = new long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			tmpIds[i] = Long.parseLong(ids[i]);
		}

		try {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			fileUpload.setSizeMax(104857600L);
			List fileItemList = null;

			fileItemList = fileUpload.parseRequest(request);
			if (fileItemList != null) {
				List list = new ArrayList();
				byte[] classBytes = null;
				for (Iterator iter = fileItemList.iterator(); iter.hasNext();) {
					FileItem item = (FileItem) iter.next();
					if (item.isFormField()) {
						continue;
					}
					classBytes = item.get();
					String filedName = item.getFieldName();

					System.out.println("ԭʼ�ļ�����" + filedName);
					System.out.println("�ֽڴ�С:" + item.get().length);

					JarInputStream jis = new JarInputStream(new ByteArrayInputStream(item.get()));
					JarEntry jarEntry = null;
					while ((jarEntry = jis.getNextJarEntry()) != null) {
						if ((!jarEntry.isDirectory()) && (jarEntry.getName().endsWith(".class"))) {
							String tmpStr = jarEntry.getName();
							tmpStr = StringUtils.replace(tmpStr, ".class", "");
							tmpStr = StringUtils.replace(tmpStr, "/", ".");
							list.add(tmpStr);
						}

					}

				}

				System.out.println("server_ids=" + tmp);
				String[] classNames = (String[]) (String[]) list.toArray(new String[0]);
				System.out.println("classNames=" + StringUtils.join(classNames, ","));
				log.error("�û�:" + request.getSession().getAttribute("USERNAME") + ",����hotpatch����,server_ids=" + tmp + ",classNames="
						+ StringUtils.join(classNames, ","));

				Map map = ParallelUtil.doHotPatch(30, 30, tmpIds, classBytes, classNames);
				StringBuilder sb = new StringBuilder();
				Set keys = map.keySet();
				for (Iterator iter = keys.iterator(); iter.hasNext();) {
					String item = (String) iter.next();
					if (isScript) {
						sb.append("server:" + item + ",ִ�����:" + map.get(item) + "<br/>");
					} else {
						sb.append("server:" + item + ",ִ�����:" + map.get(item) + "\r\n");
					}
				}

				if (isScript) {
					String rtn = "<script>parent.callback('HotPatchִ��<br/>���嵥" + StringUtils.join(classNames, ";<br/>") + "<br/>" + sb.toString()
							+ "')</script>";
					response.setContentType("text/html;charset=GBK");
					response.getOutputStream().println(rtn);
				} else {
					super.showInfo(response, "HotPatchִ��\r\n���嵥" + StringUtils.join(classNames, ";\r\n") + "\r\n" + sb.toString(), "UTF-8");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

			if (isScript) {
				String rtn = "<script>parent.callback('HotPatchʧ��<br/>" + ExceptionUtils.getFullStackTrace(e) + "')</script>";
				response.setContentType("text/html;charset=GBK");
				response.getOutputStream().println(rtn);
			} else {
				super.showInfo(response, "HotPatchʧ��\r\n" + ExceptionUtils.getFullStackTrace(e), "UTF-8");
			}
		}
	}
}