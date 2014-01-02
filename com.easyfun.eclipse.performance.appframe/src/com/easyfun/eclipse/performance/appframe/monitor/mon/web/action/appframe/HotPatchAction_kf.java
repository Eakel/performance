package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asiainfo.mon.util.MD5;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.session.SessionManager;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class HotPatchAction_kf extends BaseAction {
	private static transient Log log = LogFactory.getLog(HotPatchAction_kf.class);

	public void setValue2Session(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String hpPassword = request.getParameter("hpPassword");
			if (StringUtils.isBlank(hpPassword)) {
				super.showInfo(response, "必须输入HotPatch密码");
				log.error("必须输入HotPatch密码");
				return;
			}

			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
//			boolean rtn = objIMonSV.checkHotPatchPassword(hpPassword);
			
			boolean rtn = false;
			Connection conn = null;
			PreparedStatement ptmt = null;
			ResultSet rs = null;
			try {
				long count = 0L;
				conn = SessionManager.getSession().getConnection();
				ptmt = conn.prepareStatement("select count(*) from mon_hotpatch_password where  password = ? and state='K'");
				MD5 md5 = new MD5();
				String encrypt = md5.getMD5ofStr(hpPassword);
				ptmt.setString(1, encrypt);
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
				super.showInfo(response, "HotPatch密码不正确");
				log.error("HotPatch密码不正确");
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
}