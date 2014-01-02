package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.backmon;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPGrp;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ImageUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.TimeUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class BackMonAction extends BaseAction {
	private static transient Log log = LogFactory.getLog(BackMonAction.class);

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");

	@ActionPermission(type = "READ")
	public void getShowType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long grpId = Long.parseLong(request.getParameter("grpId"));
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		MonPGrp objMonPGrp = objIMonSV.getMonPGrpByGrpId(grpId);
		showInfo(response, objMonPGrp.getShowType());
	}

	/** 输出图片到Response */
	@ActionPermission(type = "READ")
	public void showImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String[] tmp = StringUtils.split(request.getParameter("infoId"), ",");
	    long[] infoId = new long[tmp.length];
	    for (int i = 0; i < infoId.length; i++) {
	      infoId[i] = Long.parseLong(tmp[i]);
	    }

	    long grpId = Long.parseLong(request.getParameter("grpId"));

	    Date start = null;
	    Date end = null;
	    String selectMethod = request.getParameter("selectMethod");
	    if (selectMethod.equalsIgnoreCase("last")) {
	      start = TimeUtil.addOrMinusHours(System.currentTimeMillis(), Integer.parseInt(request.getParameter("last")));
	      end = new Date();
	    }
	    else if (selectMethod.equalsIgnoreCase("range")) {
	      start = DATE_FORMAT.parse(request.getParameter("start"));
	      end = DATE_FORMAT.parse(request.getParameter("end"));
	    }

	    IMonSV objIMonSV = (IMonSV)ServiceFactory.getService(IMonSV.class);
	    MonPGrp objMonPGrp = objIMonSV.getMonPGrpByGrpId(grpId);
	    String clazz = objMonPGrp.getTransformClass();
	    HashMap map = objIMonSV.getMonLRecord4Image(infoId, clazz.trim(), start, end);
	    ImageUtil.createImage(objMonPGrp.getName(), "时间", "值", map, response.getOutputStream());
	}

	@ActionPermission(type = "READ")
	public void showFlexMSArea(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String[] tmp = StringUtils.split(request.getParameter("infoId"), ",");
	    long[] infoId = new long[tmp.length];
	    for (int i = 0; i < infoId.length; i++) {
	      infoId[i] = Long.parseLong(tmp[i]);
	    }

	    long grpId = Long.parseLong(request.getParameter("grpId"));

	    Date start = null;
	    Date end = null;
	    String selectMethod = request.getParameter("selectMethod");
	    if (selectMethod.equalsIgnoreCase("last")) {
	      start = TimeUtil.addOrMinusHours(System.currentTimeMillis(), Integer.parseInt(request.getParameter("last")));
	      end = new Date();
	    }
	    else if (selectMethod.equalsIgnoreCase("range")) {
	      start = DATE_FORMAT.parse(request.getParameter("start"));
	      end = DATE_FORMAT.parse(request.getParameter("end"));
	    }

	    IMonSV objIMonSV = (IMonSV)ServiceFactory.getService(IMonSV.class);
	    MonPGrp objMonPGrp = objIMonSV.getMonPGrpByGrpId(grpId);
	    String clazz = objMonPGrp.getTransformClass();
	    HashMap map = objIMonSV.getMonLRecord4Image(infoId, clazz.trim(), start, end);

	    showInfo(response, ImageUtil.createFlexMSAreaXml(objMonPGrp.getName(), map));
	}

	@ActionPermission(type = "READ")
	/** 显示图形化信息*/
	public void showFlexMSLine(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] tmp = StringUtils.split(request.getParameter("infoId"), ","); // 3000005,3000006
		long[] infoId = new long[tmp.length];
		for (int i = 0; i < infoId.length; i++) {
			infoId[i] = Long.parseLong(tmp[i]);
		}

	    long grpId = Long.parseLong(request.getParameter("grpId"));	//3002

	    Date start = null;
	    Date end = null;
		String selectMethod = request.getParameter("selectMethod"); // last
		if (selectMethod.equalsIgnoreCase("last")) {
			//最后12个小时
			start = TimeUtil.addOrMinusHours(System.currentTimeMillis(), Integer.parseInt(request.getParameter("last"))); // -12
			end = new Date();
		} else if (selectMethod.equalsIgnoreCase("range")) {
			start = DATE_FORMAT.parse(request.getParameter("start"));
			end = DATE_FORMAT.parse(request.getParameter("end"));
		}

	    IMonSV objIMonSV = (IMonSV)ServiceFactory.getService(IMonSV.class);
	    MonPGrp objMonPGrp = objIMonSV.getMonPGrpByGrpId(grpId);
	    String clazz = objMonPGrp.getTransformClass();
	    HashMap map = objIMonSV.getMonLRecord4Image(infoId, clazz.trim(), start, end);

	    showInfo(response, ImageUtil.createFlexMSLineXml(objMonPGrp.getName(), map));
	}

	@ActionPermission(type = "READ")
	public void showFlexTotal(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String[] tmp = StringUtils.split(request.getParameter("infoId"), ",");
	    long[] infoId = new long[tmp.length];
	    for (int i = 0; i < infoId.length; i++) {
	      infoId[i] = Long.parseLong(tmp[i]);
	    }

	    long grpId = Long.parseLong(request.getParameter("grpId"));

	    Date start = null;
	    Date end = null;
	    String selectMethod = request.getParameter("selectMethod");
	    if (selectMethod.equalsIgnoreCase("last")) {
	      start = TimeUtil.addOrMinusHours(System.currentTimeMillis(), Integer.parseInt(request.getParameter("last")));
	      end = new Date();
	    }
	    else if (selectMethod.equalsIgnoreCase("range")) {
	      start = DATE_FORMAT.parse(request.getParameter("start"));
	      end = DATE_FORMAT.parse(request.getParameter("end"));
	    }

	    IMonSV objIMonSV = (IMonSV)ServiceFactory.getService(IMonSV.class);
	    MonPGrp objMonPGrp = objIMonSV.getMonPGrpByGrpId(grpId);
	    String clazz = objMonPGrp.getTransformClass();
	    HashMap map = objIMonSV.getMonLRecord4Image(infoId, clazz.trim(), start, end);
	    showInfo(response, ImageUtil.createFlexXml(objMonPGrp.getName(), map));
	}
}