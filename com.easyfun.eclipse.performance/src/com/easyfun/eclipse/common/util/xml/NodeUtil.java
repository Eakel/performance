package com.easyfun.eclipse.common.util.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


/**
 * This class provides utility methods for w3c dom Nodes
 * @author linzhaoming
 */
public class NodeUtil {
	/**
	 * This method checks to see if the two nodes passed in contain the same information.
	 *   Attributes and children are checked for equality. So this could be a very expensive
	 *   operation.   
	 *   
	 * @param oldNode
	 * @param node
	 * @return
	 */
	public static boolean nodesEqual(Node oldNode, Node node){
		// Check if the node names are the same for each node.
		//  Return false if not.
		if (oldNode.getNodeName().equals(node.getNodeName())){
			// If both nodes are elements, check to make sure the attributes
			//  are the same for both.
			if ((oldNode instanceof Element)&&(node instanceof Element)){
				NamedNodeMap oldAttr = oldNode.getAttributes();
				NamedNodeMap newAttr = node.getAttributes();
				
				for (int index = 0; index < oldAttr.getLength(); index++){
					Node attr = newAttr.getNamedItem(oldAttr.item(index).getNodeName());
					if ((attr == null) || (!oldAttr.item(index).getNodeValue().equals(attr.getNodeValue()))){
						return false;
					}
				}
				for (int index = 0; index < newAttr.getLength(); index++){
					Node attr = oldAttr.getNamedItem(newAttr.item(index).getNodeName());
					if ((attr == null) || (!newAttr.item(index).getNodeValue().equals(attr.getNodeValue()))){
						return false;
					}
				}
			}
			// Check to make sure the children of both nodes are the same.
			NodeList oldList = oldNode.getChildNodes();
			NodeList newList = node.getChildNodes();
			// If the number of children is different, they are not equal.
			if (oldList.getLength()!=newList.getLength())
				return false;
			// Check to make sure the same children are in the same order.
			for (int index = 0; index<oldList.getLength(); index++){
				if (!nodesEqual(oldList.item(index),newList.item(index)))
					return false;
			}
			// If the children and attributes are the same, return true.
			return true;
		}else{
			// Return false since the node names are different.
			return false;
		}
	}
	
	/**
	 * This method checks to see if the two nodes passed have the same tagname and have the same Id or
	 * Name (for macros) attributes 
	 * 
	 * 
	 * @param oldNode
	 * @param newNode
	 * @return
	 */
	public static boolean nodesEqualIdOrName(Node oldNode, Node newNode) {
		boolean result = false;
		if (oldNode.getNodeName().equals(newNode.getNodeName())){
			// If both nodes are elements, check to make sure the attributes
			//  are the same for both.
			if ((oldNode instanceof Element)&&(newNode instanceof Element)){
				
				NamedNodeMap oldAttr = oldNode.getAttributes();
				NamedNodeMap newAttr = newNode.getAttributes();
				
				if(oldNode.getNodeName().equals("macro") || oldNode.getNodeName().equals("macro:macro")) {
					Node oldIdNode = oldAttr.getNamedItem("name");
					String oldId = (oldIdNode!=null) ? oldIdNode.getNodeValue() : null;
					Node newIdNode = newAttr.getNamedItem("name");
					String newId = (newIdNode!=null) ? newIdNode.getNodeValue() : null;
					if(oldId!=null && oldId.equals(newId)) {
						result = true;
					}					
				} else { 
					Node oldIdNode = oldAttr.getNamedItem(XmlConstants.ID_ATTRIBUTE);
					String oldId = (oldIdNode!=null) ? oldIdNode.getNodeValue() : null;
					Node newIdNode = newAttr.getNamedItem(XmlConstants.ID_ATTRIBUTE);
					String newId = (newIdNode!=null) ? newIdNode.getNodeValue() : null;
					if(oldId!=null && oldId.equals(newId)) {
						result = true;
					}
				}
			} 
		}
		return result;
	}
	
	/**
	 * Returns the formatted label of a given element, with open and close brackets and id or name attributes
	 * 
	 * @param element
	 * @return
	 */
	public static String getElementLabel(org.w3c.dom.Element element) {
		//use the id of the item if avaible else use the name of the item.
		String tagName = element.getNodeName();

		String text = "<" + tagName + ">";
		String idString = element.getAttribute(XmlConstants.ID_ATTRIBUTE);

		text += "id : " + idString;
		return text;
	}

		
	public static int getIndex(org.w3c.dom.Element parent, org.w3c.dom.Element child){
		int result = -1;
		
		if(parent == null || child == null){
			return result;
		}
		
		if(parent.getChildNodes() != null){
			for(int index = 0; index<parent.getChildNodes().getLength();index++){
				if(parent.getChildNodes().item(index) ==child ){
					result = index;
					break;
				}
			}
		}
		return result;
	}
	
	public static String getAttributeValue(Node node, String attName){
		if(attName == null || attName.trim().length() == 0){
			return null;
		}
		NamedNodeMap attMap = node.getAttributes();
		
		for(int index=0;index<attMap.getLength();index++){
			Node aNode = attMap.item(index);
			String aName = aNode.getNodeName();
			if(attName.equals(aName)){
				return aNode.getNodeValue();
			}
		}
		return null;
	}
	
	public static String getFirstTextChild(Node node){
		NodeList nodeList = node.getChildNodes();
		if(nodeList != null){
			for(int index = 0;index<nodeList.getLength();index++){
				Node child = nodeList.item(index);
				if(child.getNodeType() == Node.TEXT_NODE){
					if(child.getNodeValue() != null && child.getNodeValue().trim().length() > 0){
						return child.getNodeValue();
					}
				}
			}
		}
		return null;
	}
	
	public static boolean insertChildAt(org.w3c.dom.Node parent, org.w3c.dom.Node child, int index){
		if(parent == null || child == null){
			return false;
		}
		NodeList childList = parent.getChildNodes();
		if(childList == null
				|| index < 0 || index > childList.getLength() -1){
			parent.appendChild(child);
			return true;
		}
		
		Node refChild = childList.item(index);
		if(refChild != null ){
			parent.insertBefore(child, refChild);
			return true;
		}
		return false;
	}
	
	public static String getLocalName(Element element){
		String localName = null;
		if (element != null) {
			localName = element.getLocalName();
			if(localName == null || localName.trim().length() == 0){
				String qualifiedName = element.getTagName();
				if(qualifiedName != null){
					int index = qualifiedName.indexOf(':');
			        if (index < 0) {
			            localName = qualifiedName;
			        } else {
			            localName = qualifiedName.substring(index+1);
			        }
				}
			}
		} 
		return localName;
	}
	
	public static boolean contains(org.w3c.dom.Node parent, org.w3c.dom.Node child){
		if(parent == null || child == null){
			return false;
		}
		NodeList childNode = parent.getChildNodes();
		for(int index=0; childNode != null && index < childNode.getLength(); index++){
			if(child.isSameNode(childNode.item(index))){
				return true;
			}
		}
		return false;
	}
	
	public static void niceRemoveChild(Node parent, Node child) {
		if(parent == null || child == null){
			return;
		}
		
		boolean done = false;

		Node previous = child.getPreviousSibling();
		if (previous != null && previous.getNodeType() == Node.TEXT_NODE) {
			Text text = (Text) previous;
			String data = text.getData();
			int index = data.lastIndexOf('\n');
			if (index != -1) {
				if (index - 1 > 0 && data.charAt(index - 1) == '\r') {
					text.deleteData(index - 1, data.length() - index + 1);
				} else {
					text.deleteData(index, data.length() - index);
				}
				done = true;
			} 
		} 

		if (!done) {
			for (Node next = child.getNextSibling(); next != null; next = next
					.getNextSibling()) {
				if (next.getNodeType() == Node.TEXT_NODE) {
					Text text = (Text) next;
					String data = text.getData();

					int index = data.indexOf('\n');
					if (index != -1) {
						if (index + 1 < data.length()
								&& data.charAt(index + 1) == '\r') {
							text.deleteData(0, index + 2);
						} else {
							text.deleteData(0, index + 1);
						}
						break;
					} 
				} else if (next.getNodeType() == Node.ELEMENT_NODE) {
					break;
				}
			}
		}
		parent.removeChild(child);
	}
	
}
