package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;

import com.ai.appframe2.complex.mbean.standard.jvm5.JVM5MonitorMBean;
import com.easyfun.eclipse.util.xml.XmlParser;

public class MBeanHelper {
	public static List<ThreadModel> getAllThreadInfo(JVM5MonitorMBean objJVM5MontiorMBean) throws Exception {
		String str = objJVM5MontiorMBean.getAllThreadInfo();
		StringBuffer sb = new StringBuffer();
		sb.append("<threadinfo>");
		sb.append(str);
		sb.append("</threadinfo>");
		
		DOMReader reader = new DOMReader();
		Document read = reader.read(XmlParser.parseString(sb.toString()));
		Element rootElement = read.getRootElement();
		List<Element> element = rootElement.elements();
		
		List<ThreadModel> list = new ArrayList<ThreadModel>();
		for (Element ele : element) {
			ThreadModel model = new ThreadModel();
			model.setRowNum(NumberUtils.toInt(ele.element("rownum").getStringValue()));
			model.setThreadId(NumberUtils.toLong(ele.element("id").getStringValue()));
			model.setState(ele.element("state").getStringValue());
			model.setThreadName(ele.element("name").getStringValue());
			model.setThreadInfo(ele.element("stack").getStringValue());
			list.add(model);
		}

		return list;
	}
}
