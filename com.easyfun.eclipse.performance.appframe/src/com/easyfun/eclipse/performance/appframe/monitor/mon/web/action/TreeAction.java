package com.easyfun.eclipse.performance.appframe.monitor.mon.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.permission.annotation.ActionPermission;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonNode;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonTree;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.servlet.BaseAction;

public class TreeAction extends BaseAction {
	/** [MON_TREE] 获取根节点 TREE_ID=1 */
	@ActionPermission(type = "READ")
	public void getRoot(HttpServletRequest request, HttpServletResponse response) throws Exception {
		IMonSV objTreeSrv = (IMonSV) ServiceFactory.getService(IMonSV.class);
		MonTree objVMonTree = objTreeSrv.getRootTree();

		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>\n");
		sb.append("<tree id=\"" + objVMonTree.getParentId() + "\">\n");
		sb.append("<item text=\"" + objVMonTree.getName() + "\" id=\"" + objVMonTree.getTreeId() + "\" child=\"1\"/>\n");
		sb.append("</tree>\n");
		showHtmlInfo(response, sb.toString());
	}

	/** 获取子节点 [MON_TREE] [MON_NODE] */
	@ActionPermission(type = "READ")
	public void getChild(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long id = Long.parseLong(request.getParameter("id"));
		IMonSV objTreeSrv = (IMonSV) ServiceFactory.getService(IMonSV.class);

		// 目录节点
		MonTree[] objVMonTree = objTreeSrv.getChildTree(id);
		boolean isAppend = false;
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>\n");

		if ((objVMonTree != null) && (objVMonTree.length != 0)) {
			sb.append("<tree id=\"" + id + "\">\n");
			for (int i = 0; i < objVMonTree.length; i++) {
				sb.append("<item  text=\"" + objVMonTree[i].getName() + "\" id=\"" + objVMonTree[i].getTreeId() + "\" child=\"1\"/>\n");
			}
			sb.append("</tree>\n");
			isAppend = true;
		}

		// 子节点 url
		MonNode[] objVMonNode = objTreeSrv.getChildNode(id);
		if ((objVMonNode != null) && (objVMonNode.length != 0)) {
			sb.append("<tree id=\"" + id + "\">\n");
			for (int i = 0; i < objVMonNode.length; i++) {
				if (StringUtils.isBlank(objVMonNode[i].getNodeImg())) {
					if (!StringUtils.isBlank(objVMonNode[i].getUrl())) {
						sb.append("<item text=\"" + objVMonNode[i].getName() + "\" id=\"" + objVMonNode[i].getNodeId() + "\">\n");
						sb.append("<userdata name=\"url\">" + objVMonNode[i].getUrl() + "</userdata>\n");
						sb.append("</item>\n");
					} else {
						sb.append("<item text=\"" + objVMonNode[i].getName() + "\" id=\"" + objVMonNode[i].getNodeId() + "\"/>\n");
					}

				} else if (!StringUtils.isBlank(objVMonNode[i].getUrl())) {
					sb.append("<item text=\"" + objVMonNode[i].getName() + "\" id=\"" + objVMonNode[i].getNodeId() + "\" im0=\"" + objVMonNode[i].getNodeImg()
							+ "\" im1=\"" + objVMonNode[i].getNodeImg() + "\" im2=\"" + objVMonNode[i].getNodeImg() + "\" >\n");
					sb.append("<userdata name=\"url\">" + objVMonNode[i].getUrl() + "</userdata>\n");
					sb.append("</item>\n");
				} else {
					sb.append("<item text=\"" + objVMonNode[i].getName() + "\" id=\"" + objVMonNode[i].getNodeId() + "\" im0=\"" + objVMonNode[i].getNodeImg()
							+ "\" im1=\"" + objVMonNode[i].getNodeImg() + "\" im2=\"" + objVMonNode[i].getNodeImg() + "\" />\n");
				}
			}

			sb.append("</tree>\n");
			isAppend = true;
		}

		if (!isAppend) {
			sb.append("<tree id=\"" + id + "\">");
			sb.append("</tree>\n");
		}
		showHtmlInfo(response, sb.toString());
	}

	@ActionPermission(type = "READ")
	public void getChildTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long id = Long.parseLong(request.getParameter("id"));
		IMonSV objTreeSrv = (IMonSV) ServiceFactory.getService(IMonSV.class);
		MonTree[] objVMonTree = objTreeSrv.getChildTree(id);

		boolean isAppend = false;
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>\n");

		if ((objVMonTree != null) && (objVMonTree.length != 0)) {
			sb.append("<tree id=\"" + id + "\">\n");
			for (int i = 0; i < objVMonTree.length; i++) {
				sb.append("<item  text=\"" + objVMonTree[i].getName() + "\" id=\"" + objVMonTree[i].getTreeId() + "\" child=\"1\"/>\n");
			}
			sb.append("</tree>\n");
			isAppend = true;
		}

		if (!isAppend) {
			sb.append("<tree id=\"" + id + "\">");
			sb.append("</tree>\n");
		}
		showHtmlInfo(response, sb.toString());
	}
}