package FileParser;

import java.io.File;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParser {

	public XmlDataModel xmlData = null;
	
	private final String MQ_URLTODO_NAME="control/messager/urltodo(name)";
	private final String MQ_URLTODO_ACTIVATED="control/messager/urltodo(activated)";
	private final String MQ_URLTODO_IP="control/messager/urltodo/ip";
	private final String MQ_URLTODO_PORT="control/messager/urltodo/port";
	
	private final String MQ_URLFILTER_NAME="control/messager/urlfilter(name)";
	private final String MQ_URLFILTER_ACTIVATED="control/messager/urlfilter(activated)";
	private final String MQ_URLFILTER_IP="control/messager/urlfilter/ip";
	private final String MQ_URLFILTER_PORT="control/messager/urlfilter/port";
	
	private final String DB_Filter_NAME="control/db/dbfilter(name)";
	private final String DB_Filter_IP="control/db/dbfilter/ip";
	private final String DB_Filter_PORT="control/db/dbfilter/port";
	private final String DB_Filter_DATABASE="control/db/dbfilter/database";
	private final String DB_Filter_TABLE="control/db/dbfilter/table";
	private final String DB_Filter_ACTIVATED="control/db/dbfilter(activated)";
	
	private final String DB_PAGE_NAME="control/db/dbpage(name)";
	private final String DB_PAGE_IP="control/db/dbpage/ip";
	private final String DB_PAGE_PORT="control/db/dbpage/port";
	private final String DB_PAGE_DATABASE="control/db/dbpage/database";
	private final String DB_PAGE_TABLE="control/db/dbpage/table";
	private final String DB_PAGE_ACTIVATED="control/db/dbpage(activated)";
	
	private final String DL_ACTIVATED="control/downloader(activated)";
	private final String DL_COUNT="control/downloader/count";
	private final String DL_DELAY="control/downloader/delay";
	private final String DL_DELAY_UNIT="control/downloader/delay(unit)";
	
	public XmlParser() {
		try {
			xmlData = new XmlDataModel();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("bin/conf/conf.xml");
			Document document = builder.parse(file);
			Element root = document.getDocumentElement();
			
			xmlData.urlTODO.name = getValueFromPath(root, MQ_URLTODO_NAME);
			xmlData.urlTODO.isActivated = Boolean.valueOf(getValueFromPath(root, MQ_URLTODO_ACTIVATED));
			xmlData.urlTODO.ip = getValueFromPath(root, MQ_URLTODO_IP);
			xmlData.urlTODO.port = getValueFromPath(root, MQ_URLTODO_PORT);
			
			xmlData.urlFilter.name = getValueFromPath(root, MQ_URLFILTER_NAME);
			xmlData.urlFilter.isActivated = Boolean.valueOf(getValueFromPath(root, MQ_URLFILTER_ACTIVATED));
			xmlData.urlFilter.ip = getValueFromPath(root, MQ_URLFILTER_IP);
			xmlData.urlFilter.port = getValueFromPath(root, MQ_URLFILTER_PORT);
			
			xmlData.filterDB.name = getValueFromPath(root, DB_Filter_NAME);
			xmlData.filterDB.isActivated = Boolean.valueOf( getValueFromPath(root, DB_Filter_ACTIVATED));
			xmlData.filterDB.ip = getValueFromPath(root, DB_Filter_IP);
			xmlData.filterDB.port = getValueFromPath(root, DB_Filter_PORT);
			xmlData.filterDB.database = getValueFromPath(root, DB_Filter_DATABASE);
			xmlData.filterDB.table = getValueFromPath(root, DB_Filter_TABLE);
			
			xmlData.pageDB.name = getValueFromPath(root, DB_PAGE_NAME);
			xmlData.pageDB.isActivated = Boolean.valueOf( getValueFromPath(root, DB_PAGE_ACTIVATED));
			xmlData.pageDB.ip = getValueFromPath(root, DB_PAGE_IP);
			xmlData.pageDB.port = getValueFromPath(root, DB_PAGE_PORT);
			xmlData.pageDB.database = getValueFromPath(root, DB_PAGE_DATABASE);
			xmlData.pageDB.table = getValueFromPath(root, DB_PAGE_TABLE);
			
			xmlData.downloader.isActivated = Boolean.valueOf(getValueFromPath(root, DL_ACTIVATED));
			xmlData.downloader.count = Integer.parseInt( getValueFromPath(root, DL_COUNT));
			xmlData.downloader.unit = getValueFromPath(root, DL_DELAY_UNIT);
			xmlData.downloader.delay = Integer.parseInt( getValueFromPath(root, DL_DELAY));
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			
		}
	}

	/**
	 * remove the invalid nodes in NodeList that contains like "#text"
	 * 
	 * @param nodeList: input NodeList
	 * @return NodeList that removed invalid nodes
	 */
	public NodeList removeInvalidNode(NodeList nodeList) {
		int length = nodeList.getLength();
		for (int i = length - 1; i >= 0; --i) {
			Node node = nodeList.item(i);
			if (node.toString().contains("#text") || node.toString().contains("#comment")) {
				Node node1 = nodeList.item(i);
				node1.getParentNode().removeChild(node1);
			}
		}
		// for(int i = 0; i < nodeList.getLength(); ++i){
		// System.out.println(nodeList.item(i).toString());
		// }
		return nodeList;
	}
	
	/**
	 * decide to find attribute value or node value
	 * according to whether path contains '(' character
	 * @param node: the root element of current tree
	 * @param path: self-designed path like XPath 
	 * 				e.g. "node/node1/node2"
	 * 					means find node value of "node2"
	 * 				e.g. "node/node1/node2(name)"
	 * 					means find attribute value of "name" in node "node2"
	 * @return
	 */
	public String getValueFromPath(Node node, String path){
		if(path.indexOf('(') != -1){	//contain a '(' means its a attribute kind path
			return getAttributeValueFromPath(node, path);
		}else{
			return getNodeValueFromPath(node, path);
		}
	}
	
	/**
	 * find attribute value of a given path
	 * @param node: node is the current root element of the tree
	 * @param path: self-designed path like XPath 
	 * 				e.g.: "node/node1/node2(name)"
	 *            	means in the XML tree, find node2 according to the path,
	 *             	'name': name is the attribute you want to find value of
	 * @return the value of attribute
	 */
	public String getAttributeValueFromPath(Node node, String path) {
		String attr = null;
		if (node == null) return null;
		if (path == null) return null;
		if (!path.contains("/")) {
			int start = path.indexOf("(") + 1;
			int end = path.length() - 1;
			path = path.substring(start, end);
			attr = getNodeAttribute(node, path);
			return attr;
		}
		NodeList nodeChildren = node.getChildNodes();
		int start = path.indexOf('/') + 1;
		String childPath = path.substring(start);
		int end = (path.substring(start).indexOf('/')==-1) ?
				path.indexOf('(') : (path.substring(start).indexOf('/') + start);
		// if(childPath.indexOf('/')==-1){
		// end = path.indexOf('(');
		// }else{
		// end = childPath.indexOf('/') + start;
		// }
		String childNodeName = path.substring(start, end);
		int childrenCount = nodeChildren.getLength();
		for (int i = 0; i < childrenCount; ++i) {
			String nodeName = nodeChildren.item(i).getNodeName();
			if ( nodeName.equals( childNodeName)) {
				attr = getAttributeValueFromPath(nodeChildren.item(i), childPath);
				return attr;
			}
		}
		return null;
	}

	/**
	 * find node value of a given path
	 * @param node: node is the current root element of the tree. 
	 * @param path: self-designed path like XPath 
	 * 				e.g.: "node/node1/node2"
	 *            	means in the XML tree, find value of node2 according to the path, 
	 *            
	 * @return value of the node
	 */
	public String getNodeValueFromPath(Node node, String path) {
		String value = null;
		if(node == null) return null;
		if(path == null) return null;
		if(!path.contains("/")){
			value = node.getTextContent();
			return value;
		}
		
		int start = path.indexOf('/') + 1;
		int end = (path.substring(start).indexOf('/') == -1) ?
				path.length() : path.substring(start).indexOf('/') + start;
		String childNodeName = path.substring(start, end);
		
		NodeList nodeChildren = node.getChildNodes();
		int childrenCount = nodeChildren.getLength();
		for(int i = 0; i < childrenCount; ++i){
			String nodeName = nodeChildren.item(i).getNodeName();
			if( nodeName.equals(childNodeName)){
				value = getNodeValueFromPath(nodeChildren.item(i), path.substring(start));
				return value;
			}
		}
		return null;
	}
	
	/**
	 * get the target attribute's value in node's attributes
	 * @param node: this node that contains attribute 'name'
	 * @param name: the attribute's name
	 * @return String type attribute value
	 */
	public String getNodeAttribute(Node node, String name) {
		String value = null;
		NamedNodeMap nameNodeMap = node.getAttributes();
		for (int i = 0; i < nameNodeMap.getLength(); ++i) {
			if (nameNodeMap.item(i).getNodeName().equals(name)) {
				value = nameNodeMap.item(i).getNodeValue();
			}
		}
		return value;
	}
}
