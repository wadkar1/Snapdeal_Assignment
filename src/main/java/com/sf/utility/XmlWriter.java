package com.sf.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jcabi.xml.XMLDocument;

public class XmlWriter {

	String filepath = "src/test/resources/TestData/mas.xml";

	public void updateHeaderData(String invoiceNumber, String futureDate, String firstName)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);
		Node company = doc.getFirstChild();

		Node header = doc.getElementsByTagName("header").item(0);
		NodeList list = header.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);

			if ("invoice_number".equals(node.getNodeName())) {
				node.setTextContent(invoiceNumber);
			}

			if ("service_starts".equals(node.getNodeName())) {
				node.setTextContent(futureDate);
			}

			if ("service_ends".equals(node.getNodeName())) {
				node.setTextContent(futureDate);
			}

			if ("recipient_first".equals(node.getNodeName())) {
				node.setTextContent(firstName);
			}
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);
		Log.info("Updated the Header in Payload XML Successfully");

	}

	public void updateLegData(int number, String updated_leg_id, String futureDate, String futureTime)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);
		Node company = doc.getFirstChild();

		Node header = doc.getElementsByTagName("leg").item(number);

		NodeList list = header.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);

			if ("leg_id".equals(node.getNodeName())) {
				node.setTextContent(updated_leg_id);
			}

			if ("pickup_date".equals(node.getNodeName())) {
				node.setTextContent(futureDate);
			}

			if ("pickup_time".equals(node.getNodeName())) {
				node.setTextContent(futureTime);
			}

		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);
		Log.info("Updated the leg id and date for leg number " + number + " successfully");

	}

	public void updateServiceData(int number, String updated_leg_id)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);
		Node company = doc.getFirstChild();

		Node header = doc.getElementsByTagName("service").item(number);

		NodeList list = header.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);

			if ("service_leg_id".equals(node.getNodeName())) {
				node.setTextContent(updated_leg_id);
			}

		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);
		Log.info("Updated the leg id for service number " + number + " successfully");

	}

	/*
	 * public String xmlToString() throws SAXException, IOException,
	 * ParserConfigurationException, TransformerFactoryConfigurationError,
	 * TransformerException { DocumentBuilderFactory documentBuilderFactory =
	 * DocumentBuilderFactory.newInstance(); InputStream inputStream = new
	 * FileInputStream(new File(filepath)); org.w3c.dom.Document doc =
	 * documentBuilderFactory.newDocumentBuilder().parse(inputStream); StringWriter
	 * stw = new StringWriter(); Transformer serializer =
	 * TransformerFactory.newInstance().newTransformer(); serializer.transform(new
	 * DOMSource(doc), new StreamResult(stw)); return stw.toString(); }
	 */

	public String xmlToString() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));
		String line;
		StringBuilder sb = new StringBuilder();

		while ((line = br.readLine()) != null) {
			sb.append(line.trim());
		}
		return sb.toString().replaceAll("\"", "'");
	}

	public void updateDOBData(String date)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);
		Node company = doc.getFirstChild();

		Node header = doc.getElementsByTagName("header").item(0);
		NodeList list = header.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);

			if ("birthdate".equals(node.getNodeName())) {
				node.setTextContent(date);
			}
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);
		Log.info("Updated the Date Of Birth in Payload XML Successfully");

	}

	public void updateTripStatus(String status)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);
		Node company = doc.getFirstChild();

		Node header = doc.getElementsByTagName("header").item(0);
		NodeList list = header.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);

			if ("trip_status".equals(node.getNodeName())) {
				node.setTextContent(status);
			}
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);
		Log.info("Updated the Trip status to:" + status + " in Payload XML Successfully");

	}

	public void updateLegStatus(int leg_number, String status)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);
		Node company = doc.getFirstChild();

		Node header = doc.getElementsByTagName("leg").item(leg_number);

		NodeList list = header.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);

			if ("leg_status".equals(node.getNodeName())) {
				node.setTextContent(status);
			}

		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);
		Log.info("Updated the leg status to :" + status + "for leg number " + leg_number + " successfully");

	}

	public void updateInstructions(int number, String instructions)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);
		Node company = doc.getFirstChild();

		Node header = doc.getElementsByTagName("leg").item(number);

		NodeList list = header.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);

			if ("instructions".equals(node.getNodeName())) {
				node.setTextContent(instructions);
			}

		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);
		Log.info("Updated the instructions for leg number " + number + " successfully");

	}
	
	public void updateCustomer(String instructions)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse("src/test/resources/TestData/mhsfy.xml");
		Node entreprise = doc.getFirstChild();
	    Node employee = doc.getElementsByTagName("MSFLOGSALESLINEENTITY").item(0);
	    NamedNodeMap attr = employee.getAttributes();
	    Node node = attr.getNamedItem("CUSTOMERREF");
	    node.setTextContent(instructions);
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    DOMSource src = new DOMSource(doc);
	    StreamResult res = new StreamResult(new File("src/test/resources/TestData/mhsfy.xml"));
	    transformer.transform(src, res);
	}
}
