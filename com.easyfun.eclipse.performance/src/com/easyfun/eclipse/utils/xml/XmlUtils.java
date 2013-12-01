package com.easyfun.eclipse.utils.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author linzhaoming
 *
 */
public class XmlUtils {
	public static Document buildDocument(String fileName) throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		File file = new File(fileName);
		return documentBuilderFactory.newDocumentBuilder().parse(file);
	}
	
	public static Element getElement(Element parentElement, String nodeName) {
		NodeList list = parentElement.getElementsByTagName(nodeName);
		if (list == null || list.getLength() == 0) {
			return null;
		}
		return (Element) list.item(0);
	}

	public static Element[] getElements(Element parentElement, String nodeName) {
		NodeList list = parentElement.getElementsByTagName(nodeName);
		if (list == null) {
			return new Element[0];
		}
		Element[] eles = new Element[list.getLength()];
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			eles[i] = (Element) node;
		}
		return eles;
	}
}
