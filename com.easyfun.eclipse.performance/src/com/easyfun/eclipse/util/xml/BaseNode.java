package com.easyfun.eclipse.util.xml;

import java.util.Properties;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 
 * @author linzhaoming
 *
 */
public class BaseNode {
	public Properties properties = new Properties();
	
	public BaseNode(Element element) {
		if (element == null){
			return;
		}
		NamedNodeMap list = element.getAttributes();
		for (int i = 0; i < list.getLength(); i++) {
			Node attr = list.item(i);
			if (attr != null && attr.getNodeName() != null && attr instanceof Attr) {
				properties.put( ((Attr)attr).getName(),  ((Attr)attr).getValue());
			}
		}
	}

	public String getValue(String key) {
		return properties.getProperty(key);
	}
}
