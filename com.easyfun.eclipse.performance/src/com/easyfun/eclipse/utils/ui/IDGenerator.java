package com.easyfun.eclipse.utils.ui;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.easyfun.eclipse.utils.xml.XmlConstants;

/**
 * 
 * @author linzhaoming
 *
 */
public class IDGenerator {
	

	public static String getID(String prefix, Document doc, String tag) {
		if (isValidID(prefix, doc, tag)) {
			return prefix;
		}
		for (int index = 1;; index++) {
			String id = prefix + "_" + index;
			if (isValidID(id, doc, tag)) {
				return id;
			}
		}
	}

	public static boolean isValidID(String id, Document doc, String tag) {
		if(doc == null){
			return true;
		}
		NodeList connectionDefinitions = doc.getElementsByTagName(tag);
		if (connectionDefinitions != null
				&& connectionDefinitions.getLength() > 0) {
			Element connDefs = (Element) connectionDefinitions.item(0);
			NodeList connections = connDefs.getChildNodes();
			for (int index = 0; index < connections.getLength(); index++) {
				if (connections.item(index) instanceof Element) {
					Element connection = (Element) connections.item(index);
					if (id.equals(connection.getAttribute(XmlConstants.ID_ATTRIBUTE))) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public static List<String> geChildrenIDs(Document doc, String tag) {
		List<String> ids = new LinkedList<String>();
		NodeList connectionDefinitions = doc.getElementsByTagName(tag);
		if (connectionDefinitions != null && connectionDefinitions.getLength() > 0) {
			Element connDefs = (Element) connectionDefinitions.item(0);
			NodeList connections = connDefs.getChildNodes();
			for (int index = 0; index < connections.getLength(); index++) {
				if (connections.item(index) instanceof Element) {
					Element connection = (Element) connections.item(index);
					ids.add(connection.getAttribute(XmlConstants.ID_ATTRIBUTE));
				}
			}
		}
		return ids;
	}
}
