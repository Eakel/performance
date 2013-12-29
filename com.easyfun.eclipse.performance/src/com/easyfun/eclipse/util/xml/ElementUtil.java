package com.easyfun.eclipse.util.xml;

import org.w3c.dom.Node;

/**
 * 
 * @author linzhaoming
 *
 */
public class ElementUtil {

	public static void removeAllChildren(org.w3c.dom.Element element) {
		if (element == null) {
			return;
		}
		for (int index = 0; index < element.getChildNodes().getLength(); index++) {
			Node child = element.getChildNodes().item(0);
			element.removeChild(child);
		}
	}

	public static boolean canHaveColumns(org.w3c.dom.Element element) {
		if (element == null) {
			return false;
		}
		String tagName = element.getTagName();
		return element != null
				&& ("table".equals(tagName) || "tree".equals(tagName) || "treeTable"
						.equals(tagName));
	}

}
