package javatraing.javatraining_maven;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class XmlFile {
	private static Logger logger = LoggerFactory.getLogger(CmdLine.class
			.getName());
	private Document doc;
	private static XmlFile instance;

	private static int printType = 0;

	private XmlFile() {
		super();
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			doc = builder.parse(XmlFile.class
					.getResourceAsStream("/addressbook.xml"));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmlInit();
	}

	public static XmlFile getNewInstance() {
		if (instance == null) {
			instance = new XmlFile();
		}
		return instance;
	}

	public static int getPrintType() {
		return printType;
	}

	public static void setPrintType(int printType) {
		XmlFile.printType = printType;
	}

	private void xmlInit() {
		/*
		 * Element employer = doc.createElement("employers");
		 * doc.getDocumentElement().appendChild(employer);
		 */
	}

	public void flushToFile() {
		try {
			Transformer aTransformer = TransformerFactory.newInstance()
					.newTransformer();
			Source src = new DOMSource(doc);
			StreamResult dest = null;
			if (printType == 0) {
				File obj = new File(XmlFile.class.getResource(
						"/addressbook.xml").getFile());
				dest = new StreamResult(obj);
			} else {
				dest = new StreamResult(System.out);
			}
			aTransformer.transform(src, dest);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addNode(PeopleMsg peopMsg) {
		Element employer = doc.createElement("employer");
		Element name = doc.createElement("name");
		Element id = doc.createElement("id");
		Element phoneNum = doc.createElement("phoneNum");
		Element address = doc.createElement("address");
		Attr attr = doc.createAttribute("id");
		attr.setValue(peopMsg.getId());

		Text txtName = doc.createTextNode(peopMsg.getName());
		Text txtId = doc.createTextNode(peopMsg.getId());

		Text txtAddress = doc.createTextNode(peopMsg.getAddress());
		Text txtPhoneNum = doc.createTextNode(peopMsg.getPhoneNumber());
		name.appendChild(txtName);
		id.appendChild(txtId);

		phoneNum.appendChild(txtPhoneNum);
		address.appendChild(txtAddress);

		employer.appendChild(name);
		employer.appendChild(id);
		employer.appendChild(phoneNum);
		employer.appendChild(address);

		employer.setAttribute("id", peopMsg.getId());

		Element empRoot = doc.getDocumentElement();
		empRoot.appendChild(employer);
	}

	public Node removeNode(String id) {
		Node ret = null;
		Node delNode = getNodeWithAttribute(id);
		if (delNode != null) {
			ret = ((Element) delNode).cloneNode(true);
			delNode.getParentNode().removeChild(delNode);
		}
		return ret;
	}

	public boolean setNode(PeopleMsg peopMsg) {
		boolean ret = false;
		Element element = (Element) getNodeWithAttribute(peopMsg.getId());
		if (element != null) {
			Element name = (Element) element.getElementsByTagName("name").item(
					0);
			Element phoneNum = (Element) element.getElementsByTagName(
					"phoneNum").item(0);
			Element address = (Element) element.getElementsByTagName("address")
					.item(0);
			name.getFirstChild().setNodeValue(peopMsg.getName());
			address.getFirstChild().setNodeValue(peopMsg.getAddress());
			phoneNum.getFirstChild().setNodeValue(peopMsg.getPhoneNumber());
			ret = true;
		}
		return ret;
	}

	private Node getNodeWithAttribute(String nodeId) {
		Node retNode = null;
		Node root = doc.getFirstChild();
		NodeList nodelist = root.getChildNodes();
		Node employer;
		for (int l = 0; l < nodelist.getLength(); l++) {
			employer = nodelist.item(l);
			if (employer.getNodeType() == Node.ELEMENT_NODE
					&& nodeId.equals(((Element) employer).getAttribute("id"))) {
				retNode = employer;
				break;
			}
		}

		return retNode;
	}

	private Node getNodeWithSubValue(String tagName, String tagValue) {
		Node retNode = null;
		Node root = doc.getFirstChild();
		NodeList nodelist = root.getChildNodes();
		Node empRoot;
		for (int l = 0; l < nodelist.getLength(); l++) {
			empRoot = nodelist.item(l);
			if (empRoot.getNodeType() == Node.ELEMENT_NODE) {
				NodeList employer = empRoot.getChildNodes();
				for (int i = 0; i < employer.getLength(); i++) {
					Node tmpNode = employer.item(i);
					if (tmpNode.getNodeName().equals(tagName)
							&& tmpNode.getFirstChild().getTextContent()
									.equals(tagValue)) {
						retNode = empRoot;
						break;
					}
				}
			}
		}

		return retNode;
	}

	public PeopleMsg getPeopMsg(PeopleMsg peopMsg) {
		PeopleMsg people = null;
		Element empRoot = null;
		if (peopMsg.getId() != null && peopMsg.getId() != "") {
			empRoot = (Element) getNodeWithAttribute(peopMsg.getId());

		} else if (peopMsg.getName() != null && peopMsg.getName() != "") {
			empRoot = (Element) getNodeWithSubValue("name", peopMsg.getName());

		} else if (peopMsg.getAddress() != null && peopMsg.getAddress() != "") {
			empRoot = (Element) getNodeWithSubValue("address",
					peopMsg.getAddress());

		} else if (peopMsg.getPhoneNumber() != null
				&& peopMsg.getPhoneNumber() != "") {
			empRoot = (Element) getNodeWithSubValue("phoneNum",
					peopMsg.getPhoneNumber());
		}

		if (empRoot != null) {
			people = node2People(empRoot);
		}
		return people;
	}

	public PeopleMsg node2People(Node node) {
		PeopleMsg people = null;
		Element empRoot = null;
		NodeList employer = null;
		if (node != null) {
			empRoot = (Element) node;
			if (empRoot != null) {
				employer = empRoot.getChildNodes();
				people = new PeopleMsg();
				people.setId(((Element) employer).getAttribute("id"));
				for (int i = 0; i < employer.getLength(); i++) {
					Node tmpNode = employer.item(i);
					if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
						if (tmpNode.hasChildNodes()) {
							if (tmpNode.getNodeName().equals("name")) {
								people.setName(tmpNode.getFirstChild()
										.getTextContent());
							} else if (tmpNode.getNodeName().equals("address")) {
								people.setAddress(tmpNode.getFirstChild()
										.getTextContent());
							} else if (tmpNode.getNodeName().equals("phoneNum")) {
								people.setPhoneNum(tmpNode.getFirstChild()
										.getTextContent());
							}
						}
					}
				}
			}
		}
		return people;
	}
}
