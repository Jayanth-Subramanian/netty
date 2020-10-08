package com.netty.chat.server;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XMLConverter {

	public static String getXMLasString(String fileName) {

		String st = new String();
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			InputSource is = new InputSource(fileName);
			Document document = docBuilderFactory.newDocumentBuilder().parse(is);
   
			StringWriter sw = new StringWriter();
			Transformer serializer = TransformerFactory.newInstance().newTransformer();
			serializer.transform(new DOMSource(document), new StreamResult(sw));
			st = sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return st;
	}
}
