package com.easyfun.eclipse.performance.appframe.monitor.mon.test;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonTree;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;

public class Test {
	public static void main(String[] args) throws Exception{
		IMonSV objTreeSrv = (IMonSV) ServiceFactory.getService(IMonSV.class);
		MonTree objVMonTree = objTreeSrv.getRootTree();

		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>\n");
		sb.append("<tree id=\"" + objVMonTree.getParentId() + "\">\n");
		sb.append("<item text=\"" + objVMonTree.getName() + "\" id=\"" + objVMonTree.getTreeId() + "\" child=\"1\"/>\n");
		sb.append("</tree>\n");
		System.out.println(sb.toString());
	}
}
