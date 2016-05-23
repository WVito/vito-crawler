/**
 * @author vito
 * time:	2016-5-7
 */

package FileParser;

import java.io.File;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import MQController.MQParameter;
import PageDB.MongoDBParameter;

public class XmlParser {

	public MQParameter urltodoParameter;
	public MQParameter urlfilterParameter;
	public MongoDBParameter dbParameter;
	public boolean activateDownload = false;
	public int threadCount;
	public int delay;
	public String delayUnit;
	public boolean activateFilter = false;
	public int filter_size;
	public int fingerPrintCount;

	private final String MQ_URLTODO_IP = "control/messager/mq(name:urltodo)/ip";
	private final String MQ_URLTODO_PORT = "control/messager/mq(name:urltodo)/port";
	private final String MQ_URLTODO_USER = "control/messager/mq(name:urltodo)/user";
	private final String MQ_URLTODO_PASSWORD = "control/messager/mq(name:urltodo)/password";

	private final String MQ_URLFILTER_IP = "control/messager/mq(name:urlfilter)/ip";
	private final String MQ_URLFILTER_PORT = "control/messager/mq(name:urlfilter)/port";
	private final String MQ_URLFILTER_USER = "control/messager/mq(name:urlfilter)/user";
	private final String MQ_URLFILTER_PASSWORD = "control/messager/mq(name:urlfilter)/password";

	private final String DB_IP = "control/db/ip";
	private final String DB_PORT = "control/db/port";
	private final String DB_DATABASE = "control/db/database";
	private final String DB_COLLECTION = "control/db/collection";

	private final String FILTER_SIZE = "control/filter/size";
	private final String FILTER_FINGERPRINTCOUNT = "control/filter/fingerprintCount";

	private final String DL_COUNT = "control/downloader/count";
	private final String DL_DELAY = "control/downloader/delay";
	private final String DL_DELAY_UNIT = "control/downloader/unit";

	public XmlParser() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("bin/conf/conf.xml");
			Document document = builder.parse(file);
			Element root = document.getDocumentElement();
			
			if (getNodeValue(root, MQ_URLTODO_IP) != null) {
				String ip = getNodeValue(root, MQ_URLFILTER_IP);				
				String port = getNodeValue(root, MQ_URLTODO_PORT);
				String user = getNodeValue(root, MQ_URLTODO_USER);
				String password = getNodeValue(root, MQ_URLTODO_PASSWORD);
				if( user != null){
					this.urltodoParameter = new MQParameter(ip, port, "urltodo", user, password);
				}else{
					this.urltodoParameter = new MQParameter(ip, port, "urltodo");
				}
			}
			if ( getNodeValue(root, MQ_URLFILTER_IP) != null) {
				String ip = getNodeValue(root, MQ_URLFILTER_IP);
				String port = getNodeValue(root, MQ_URLFILTER_PORT);
				String user = getNodeValue(root, MQ_URLFILTER_USER);
				String password = getNodeValue(root, MQ_URLFILTER_PASSWORD);
				if( user != null ){
					this.urlfilterParameter = new MQParameter(ip, port, "urlfilter", user, password);
				}else{
					this.urlfilterParameter = new MQParameter(ip, port, "urlfilter");
				}
			}
			if ( getNodeValue(root, DB_DATABASE) != null) {
				String ip = getNodeValue(root, DB_IP);
				String port = getNodeValue(root, DB_PORT);
				String database = getNodeValue(root, DB_DATABASE);
				String collection = getNodeValue(root, DB_COLLECTION);
				this.dbParameter = new MongoDBParameter(ip, port, database, collection);
			}
			if ( getNodeValue(root, DL_DELAY_UNIT) != null) {
				this.threadCount = Integer.parseInt(getNodeValue(root, DL_COUNT));
				this.delay = Integer.parseInt(getNodeValue(root, DL_DELAY));
				this.delayUnit = getNodeValue(root, DL_DELAY_UNIT);
				this.activateDownload = true;
			}
			if (getNodeValue(root, FILTER_SIZE) != null) {
				this.filter_size = Integer.parseInt(getNodeValue(root, FILTER_SIZE));
				this.fingerPrintCount = Integer.parseInt(getNodeValue(root, FILTER_FINGERPRINTCOUNT));
				this.activateFilter = true;
			}
		} catch (Exception e) {
			System.err.print("parse xml file wrong: ");
			e.printStackTrace();
		} finally {

		}
	}

	public String getNodeValue(Node root, String path) {
		String value = null;
		if (root == null)
			return null;
		if (path == null)
			return null;
		if (!path.contains("/")) { // leaf node
			String nodeName = getNodeName(path);
			String attrName = getAttributeName(path);
			String attrValue = getAttributeValue(path);
			if (root.getNodeName().equals(nodeName) && containsAttribute(root, attrName, attrValue))
				return root.getTextContent();
		}
		String subPath = path.substring(path.indexOf("/") + 1);
		String subName = getNodeName(subPath);
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node item = children.item(i);
			if (item.getNodeName().equals(subName)) {
				value = getNodeValue(item, subPath);
				if (value != null)
					return value;
			}
		}
		return null;
	}

	public boolean containsAttribute(Node node, String attrName, String attrValue) {
		NamedNodeMap attrs = node.getAttributes();
		if (attrs.getLength() == 0) {
			if (attrName == null)
				return true;
			return false;
		}
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			if (attr.getNodeName() == attrName && attr.getNodeValue() == attrValue)
				return true;
		}
		return false;
	}

	public String getNodeName(String path) {
		String fullNodeName = getFullNodeName(path);
		if (fullNodeName == null || fullNodeName == "")
			return null;
		String nodeName = null;
		int indexOfRoundBrackets = fullNodeName.indexOf("(");
		if (indexOfRoundBrackets == -1) { // not contain "(", no attribute
			nodeName = fullNodeName;
		} else {
			nodeName = fullNodeName.substring(0, indexOfRoundBrackets);
		}
		if (nodeName == null || nodeName == "")
			return null;
		return nodeName;
	}

	public String getAttributeName(String path) {
		String fullNodeName = getFullNodeName(path);
		if (fullNodeName == null || fullNodeName == "")
			return null;
		int indexL = fullNodeName.indexOf("(");
		int indexR = fullNodeName.indexOf(")");
		int indexColon = fullNodeName.indexOf(":");
		if (indexL == -1 || indexR == -1 || indexColon == -1)
			return null;
		String attrName = fullNodeName.substring(indexL + 1, indexColon);
		if (attrName == null || attrName == "")
			return null;
		return attrName;
	}

	public String getAttributeValue(String path) {
		String fullNodeName = getFullNodeName(path);
		if (fullNodeName == null)
			return null;
		int indexL = fullNodeName.indexOf("(");
		int indexR = fullNodeName.indexOf(")");
		int indexColon = fullNodeName.indexOf(":");
		if (indexL == -1 || indexR == -1 || indexColon == -1)
			return null;
		String attrValue = fullNodeName.substring(indexColon + 1, indexR);
		if (attrValue == null || attrValue == "")
			return null;
		return attrValue;
	}

	public String getFullNodeName(String path) {
		if (path == null || path == "")
			return null;
		String fullNodeName = null;
		int index = path.indexOf("/");
		if (index == -1) {
			fullNodeName = path;
		} else {
			fullNodeName = path.substring(0, index);
		}
		if (fullNodeName == null || fullNodeName == "")
			return null;
		return fullNodeName;
	}

	/**
	 * get parameter of MQ "urltodo" in tree root
	 * 
	 * @param root
	 * @return if find in XML file, return MQparameter, otherwise return null.
	 */
	public MQParameter getURLTODOParameter(Node root) {
		MQParameter parameter = null;
		Node urltodoMQNode = getNodeByNameAndAttribute(root, "mq", "name", "urltodo");
		if (urltodoMQNode == null)
			return null;
		String ip = getFirstSubNodeValueByName(urltodoMQNode, "ip");
		String port = getFirstSubNodeValueByName(urltodoMQNode, "port");
		String queueName = "urltodo";
		String user = getFirstSubNodeValueByName(urltodoMQNode, "user");
		String pwd = getFirstSubNodeValueByName(urltodoMQNode, "password");
		if (user == null || pwd == null) {
			parameter = new MQParameter(ip, port, queueName);
		} else {
			parameter = new MQParameter(ip, port, queueName, user, pwd);
		}
		return parameter;
	}

	/**
	 * get parameter of MQ "urlfilter" in tree root
	 * 
	 * @param root
	 * @return if find in XML file, return MQParameter, otherwise return null.
	 */
	public MQParameter getURLFilterParameter(Node root) {
		MQParameter parameter = null;
		Node urlfilterNode = getNodeByNameAndAttribute(root, "mq", "name", "urlfilter");
		if (urlfilterNode == null)
			return null;
		String ip = getFirstSubNodeValueByName(urlfilterNode, "ip");
		String port = getFirstSubNodeValueByName(urlfilterNode, "port");
		String queueName = "urlfilter";
		String user = getFirstSubNodeValueByName(urlfilterNode, "user");
		String pwd = getFirstSubNodeValueByName(urlfilterNode, "password");
		if (user == null || pwd == null) {
			parameter = new MQParameter(ip, port, queueName);
		} else {
			parameter = new MQParameter(ip, port, queueName, user, pwd);
		}
		return parameter;
	}

	/**
	 * get parameter of MongoDB in XML file, return MQParameter, otherwise
	 * return null.
	 * 
	 * @param root
	 * @return if find in XML file, return MongoDBParameter, otherwise return
	 *         null.
	 */
	public MongoDBParameter getDBParameter(Node root) {
		MongoDBParameter parameter = null;
		Node dbNode = getNodeByNameAndAttribute(root, "db", "", "");
		if (dbNode == null)
			return null;
		String ip = getFirstSubNodeValueByName(dbNode, "ip");
		String port = getFirstSubNodeValueByName(dbNode, "port");
		String dbName = getFirstSubNodeValueByName(dbNode, "database");
		String collectionName = getFirstSubNodeValueByName(dbNode, "collection");
		parameter = new MongoDBParameter(ip, port, dbName, collectionName);
		return parameter;
	}

	/**
	 * get node by node name and attribute name it will return the first node
	 * that matches
	 * 
	 * @param root
	 *            current node
	 * @param nodeName
	 *            the node name to find
	 * @param attName
	 *            the attribute name to find
	 * @param attNameValue
	 *            the attribute value to find
	 * @return he first node that matches
	 */
	public Node getNodeByNameAndAttribute(Node root, String nodeName, String attName, String attNameValue) {
		Node nd = null;
		if (root.getNodeName().equals(nodeName) && containsAtt(root, attName, attNameValue)) {
			return root;
		}
		NodeList nodeList = root.getChildNodes();
		nodeList = removeInvalidNode(nodeList);
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeName() == "filter") {
				int a = 0;
			}
			nd = getNodeByNameAndAttribute(nodeList.item(i), nodeName, attName, attNameValue);
			if (nd != null)
				return nd;
		}
		return nd;
	}

	/**
	 * find if node has attribute named attName
	 * 
	 * @param root
	 *            the target node
	 * @param attName
	 *            the target attribute name
	 * @param attNameValue
	 *            the attribute's value
	 * @return if contains return true, otherwise false.
	 */
	public boolean containsAtt(Node root, String attName, String attNameValue) {
		NamedNodeMap atts = root.getAttributes();
		int len = atts.getLength();
		if (len != 0) {
			for (int i = 0; i < len; i++) {
				Node node = atts.item(i);
				if (node.getNodeName().equals(attName) && node.getNodeValue().equals(attNameValue)) {
					return true;
				}
			}
		} else {
			if (attName == "") {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * get first sub-node value by node name in recursion way.
	 * 
	 * @param node
	 *            find
	 * @param subNodeName
	 * @return it will return node value if find the node, otherwise not.
	 */
	public String getFirstSubNodeValueByName(Node node, String subNodeName) {
		String value = null;
		if (node == null)
			return null;
		if (subNodeName == null)
			return null;
		if (node.getNodeName().equals(subNodeName)) {
			String textContent = node.getTextContent();
			return textContent;
		}
		NodeList nodelist = node.getChildNodes();
		nodelist = removeInvalidNode(nodelist);
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node item = nodelist.item(i);
			value = getFirstSubNodeValueByName(item, subNodeName);
			if (value != null)
				return value;
		}
		return null;
	}

	/**
	 * remove the invalid nodes in NodeList that contains like "#text",
	 * "#comment"
	 * 
	 * @param nodeList:
	 *            input NodeList
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
		return nodeList;
	}

	/**
	 * get the target attribute's value in node's attributes
	 * 
	 * @param node:
	 *            this node that contains attribute 'name'
	 * @param name:
	 *            the attribute's name
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
