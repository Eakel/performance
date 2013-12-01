package com.easyfun.eclipse.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.easyfun.eclipse.utils.ui.dialog.SafeMessageDialog;

/**
 * Utility class to help with xml
 * @author linzhaoming
 */
public class XmlParser {

	private static String _fileEncoding = "UTF-8";

	/**
	 * Save the specified document to the specified file location
	 * @param fileName - The location of the file to save the document to
	 * @param doc - The documennt to save to the file.
	 */
	public static void save(String fileName, Document doc) {
		File file = new File(fileName);
		//save the file with the "File" method
		save(file, doc);
	}

	/**
	 * Save the specified document to the specified file location
	 * @param fileName - The location of the file to save the document to
	 * @param doc - The documennt to save to the file.
	 */
	public static void save(File file, Document doc) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");

			FileOutputStream outputStream = new FileOutputStream(file);

			transformer.transform(new DOMSource(doc, _fileEncoding), new StreamResult(outputStream));
			outputStream.close();
		} catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		} catch (TransformerConfigurationException transformerConfigurationException) {
			transformerConfigurationException.printStackTrace();
		} catch (TransformerException transformerException) {
			transformerException.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save the specified document to the specified file location
	 * @param fileName - The location of the file to save the document to
	 * @param doc - The documennt to save to the file.
	 * @param docTypePublic - The public document type.
	 * @param docTypeSystem - The document type system.
	 */
	public static void save(String fileName, Document doc, String docTypePublic, String docTypeSystem) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;

		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");

			DocumentType documentType = doc.getDoctype();
			if (documentType != null) {
				transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, docTypePublic);
				transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docTypeSystem);
			}

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");

			FileOutputStream outputStream = new FileOutputStream(fileName);

			transformer.transform(new DOMSource(doc, _fileEncoding), new StreamResult(outputStream));
			outputStream.close();
		} catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		} catch (TransformerConfigurationException transformerConfigurationException) {
			transformerConfigurationException.printStackTrace();
		} catch (TransformerException transformerException) {
			transformerException.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String convertNodeToString(Node node) {
		return convertNodeToString(node, true);
	}

	/**
	 * Convert the supplied string into an xml string
	 * @param node
	 * @return
	 */
	public static String convertNodeToString(Node node, boolean omitXmlDecl) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		String xmlString = "";
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
			if (omitXmlDecl == true) {
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			}
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //$NON-NLS-1$ //$NON-NLS-2$

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			transformer.transform(new DOMSource(node, _fileEncoding), new StreamResult(byteArrayOutputStream));

			xmlString = byteArrayOutputStream.toString();
			byteArrayOutputStream.close();
		} catch (TransformerConfigurationException transformerConfigurationException) {
			transformerConfigurationException.printStackTrace();
		} catch (TransformerException transformerException) {
			transformerException.printStackTrace();
		} catch (UnsupportedEncodingException unsupportedEncodingException) {
			unsupportedEncodingException.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return xmlString;
	}

	/**
	 * convert the supplied string into a dom and then updates the table information
	 * @param xmlString
	 */
	public static Document parseString(String xmlString) {

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			ByteArrayInputStream stream = new ByteArrayInputStream(xmlString.getBytes());

			//parse the stream into a document
			return documentBuilder.parse(stream);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * convect the supplied document to a string
	 * @param document
	 * @return
	 */
	public static String convertDocumentToString(Document document) {
		return convertNodeToString(document);
	}

	public static String getStringValue(Document document, String cacheType) {
		String returnString = "";
		if (document == null)
			return returnString;

		NodeList nodeList = document.getElementsByTagName(cacheType);
		Node childNode = nodeList.item(0);
		if (childNode == null)
			return returnString;

		String value = getTextNodeValue(childNode);
		if (value == null)
			return returnString;

		returnString = value;
		return returnString;
	}

	public static String getStringValue(Element element, String cacheType) {
		String returnString = "";
		if (element == null)
			return returnString;

		NodeList nodeList = element.getElementsByTagName(cacheType);
		Node childNode = nodeList.item(0);
		if (childNode == null)
			return returnString;

		String value = getTextNodeValue(childNode);
		if (value == null)
			return returnString;

		returnString = value;
		return returnString;
	}

	public static String getTextNodeValue(Node node) {
		String returnString = "";
		if (node == null)
			return returnString;

		Node textNode = node.getFirstChild();
		if (textNode instanceof org.w3c.dom.Text) {
			returnString = textNode.getNodeValue();
		}
		return returnString;
	}

	public static boolean getBooleanValue(Document document, String cacheType) {
		boolean bResult = false;
		if (document == null)
			return bResult;

		String value = getStringValue(document, cacheType);
		bResult = value.equals("true") == true ? true : false;

		return bResult;
	}

	public static boolean getBooleanValue(Element element) {
		boolean bResult = false;
		if (element == null)
			return bResult;

		String value = getTextNodeValue(element);
		bResult = value.equals("true") == true ? true : false;

		return bResult;
	}

	public static void setStringValue(Document document, String cacheType, String value) {
		if (document == null)
			return;

		NodeList nodeList = document.getElementsByTagName(cacheType);
		Node childNode = null;
		if (nodeList != null && nodeList.getLength() > 0) {
			childNode = nodeList.item(0);
		}

		if (childNode == null) {
			childNode = document.createElement(cacheType);
			document.getLastChild().appendChild(childNode);
		}

		Node node = childNode.getFirstChild();
		if (node instanceof org.w3c.dom.Text) {
			node.setNodeValue(value);
		} else {
			org.w3c.dom.Text text = document.createTextNode(value);
			childNode.appendChild(text);
		}
	}

	public static void setStringValue(Document document, Element element, String value) {
		if (document == null)
			return;

		Node node = element.getFirstChild();
		if (node instanceof org.w3c.dom.Text) {
			node.setNodeValue(value);
		} else {
			org.w3c.dom.Text text = document.createTextNode(value);
			element.appendChild(text);
		}
	}

	public static Node setStringValue(Document document, Element parent, String nodeName, String value) {
		//if the node doens't exist then create a new one and
		Element element = getFirstElementByTagName((Element) parent, nodeName);
		if (element == null) {
			element = document.createElement(nodeName);
			parent.appendChild(element);
		}

		//set the text node value for the node
		Node textNode = element.getFirstChild();
		if (textNode instanceof org.w3c.dom.Text) {
			textNode.setNodeValue(value);
		} else {
			org.w3c.dom.Text text = document.createTextNode(value);
			element.appendChild(text);
		}

		return element;
	}

	/**
	 * Set the value of the supplied text of remove the object if the value is empty
	 * @param document
	 * @param parent
	 * @param nodeName
	 * @param value
	 * @return
	 */
	public static Node setStringValueOrRemove(Document document, Element parent, String nodeName, String value) {
		//if the node doens't exist then create a new one and
		Element element = getFirstElementByTagName((Element) parent, nodeName);
		if (element == null) {
			element = document.createElement(nodeName);
			parent.appendChild(element);
		}

		//if hte value is empty remove it from the parent,
		if (value == null || value.length() == 0) {
			parent.removeChild(element);
			return null;
		}

		//set the text node value for the node
		Node textNode = element.getFirstChild();
		if (textNode instanceof org.w3c.dom.Text) {
			textNode.setNodeValue(value);
		} else {
			org.w3c.dom.Text text = document.createTextNode(value);
			element.appendChild(text);
		}

		return element;
	}

	public static void moveNode(Document document, Node newParent, String cacheType) {
		if (document == null)
			return;

		NodeList nodeList = document.getElementsByTagName(cacheType);
		Node childNode = nodeList.item(0);
		if (childNode == null)
			return;

		Node parent = childNode.getParentNode();
		if (parent == null)
			return;

		parent.removeChild(childNode);
		newParent.appendChild(childNode);
	}

	/**
	 * Go through the contexts and find the of this app. 
	 * Use the application location and as the basis of the find.
	 * if the application is in the webapp directory check for docBase of 
	 * relativc directory, if out side the webb apps directory check for the 
	 * absolute directory.
	 */
	public static Element findApplicationContext(IProject project, Document document, String lastSegment) {
		//determine if the application is in the webapp directory.
		if (project == null)
			return null;
		if (document == null)
			return null;

		IPath applicationPath = project.getLocation();
		String applicationName = project.getName();
		String absolutePath = applicationPath.toOSString();
		Element returnElement = null;

		NodeList nodeList = document.getElementsByTagName("Context");
		for (int index = 0; index < nodeList.getLength(); index++) {
			Node node = nodeList.item(index);
			if (node instanceof Element) {
				Element element = (Element) node;
				String docBase = element.getAttribute("docBase");
				String root = element.getAttribute("root");
				if (docBase == null)
					continue;

				//is this equal to the absolute path
				if (docBase.equals(absolutePath) == true) {
					returnElement = element;
					break;
				} else if (docBase.equals(lastSegment) == true) {
					returnElement = element;
					break;
				} else if ((docBase + File.separator).equals(lastSegment) == true) {
					returnElement = element;
					break;
				} else if (docBase.equals("/" + lastSegment) == true) {
					returnElement = element;
					break;
				} else if (docBase.equals("/" + lastSegment + "/") == true) {
					returnElement = element;
					break;
				} else if (docBase.equals("./" + lastSegment) == true) {
					returnElement = element;
					break;
				} else if (docBase.equals("./" + lastSegment + "/") == true) {
					returnElement = element;
					break;
				} else if (root != null && root.equals("/" + applicationName) == true) {
					returnElement = element;
					break;
				}
			}
		}
		return returnElement;
	}

	/**
	 * Get the first element of this type for the supplied element at its children.
	 * @param document
	 * @param tagName
	 * @return
	 */
	public static Element getFirstElementByTagName(Element element, String tagName) {
		if (element == null)
			return null;

		//get the list of tag mapping files
		NodeList nodeList = element.getElementsByTagName(tagName);

		for (int index = 0; index < nodeList.getLength(); index++) {
			//get the nodelist
			Node node = nodeList.item(index);
			if (tagName.equals(node.getNodeName()) == true)
				return (Element) node;
		}

		return null;
	}

	/**
	 * Get the first element of this type for the document
	 * @param document
	 * @param tagName
	 * @return
	 */
	public static Element getFirstElementByTagName(Document document, String tagName) {
		if (document == null)
			return null;

		//get the list of tag mapping files
		NodeList nodeList = document.getElementsByTagName(tagName);

		for (int index = 0; index < nodeList.getLength(); index++) {
			//get the nodelist
			Node node = nodeList.item(index);
			if (tagName.equals(node.getNodeName()) == true)
				return (Element) node;
		}
		return null;
	}

	/**
	 * Get all the element text of this tag name
	 * @param document
	 * @param list
	 */
	public static List<String> getElementTextByTagName(Document document, String tagName) {
		List<String> list = new ArrayList<String>();
		if (document == null)
			return list;

		//get the list of tag mapping files
		NodeList nodeList = document.getElementsByTagName(tagName);
		for (int index = 0; index < nodeList.getLength(); index++) {
			Node node = nodeList.item(index);
			String textValue = getTextNodeValue(node);
			list.add(textValue);
		}

		return list;
	}

	/**
	 * Get all the elements of this tag name from the supplied document.
	 * @param document
	 * @param vector
	 */
	public static Vector<Element> getElementsByTagName(Document document, String tagName) {
		Vector<Element> vector = new Vector<Element>();
		if (document == null)
			return vector;

		//get the list of tag mapping files
		NodeList nodeList = document.getElementsByTagName(tagName);
		for (int index = 0; index < nodeList.getLength(); index++) {
			Node node = nodeList.item(index);
			if (tagName.equals(node.getNodeName()) == true) {
				vector.add((Element) node);
			}
		}

		return vector;
	}

	/**
	 * Get all the elements of this tag name from the parent element.
	 * @param document
	 * @param vector
	 */
	public static Vector<Element> getElementsByTagName(Element element, String tagName) {
		Vector<Element> vector = new Vector<Element>();
		if (element == null)
			return vector;

		//get the list of tag mapping files
		NodeList nodeList = element.getElementsByTagName(tagName);
		for (int index = 0; index < nodeList.getLength(); index++) {
			Node node = nodeList.item(index);
			if (tagName.equals(node.getNodeName()) == true) {
				vector.add((Element) node);
			}
		}

		return vector;
	}

	public static Document createEmptyDocument() {
		Document doc = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			doc = documentBuilder.newDocument(); 
		} catch (Exception ex) {
			SafeMessageDialog.errorMessage("Nexaweb Studio - XML Parse Error", "Failed to parse input stream.\n\nReason:  " + ex.getMessage());
		}
		return doc;
	}

}
