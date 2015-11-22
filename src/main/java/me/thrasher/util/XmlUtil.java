package me.thrasher.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lombok.extern.java.Log;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Log
public class XmlUtil {
	public static Document asXml(String s) throws SAXException {
		DocumentBuilder db;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			log.log(Level.WARNING, e.getMessage(), e);
			throw new RuntimeException(e);
		}
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(s));
		Document doc = null;
		try {
			doc = db.parse(is);
		} catch (SAXException e) {
			log.log(Level.WARNING, e.getMessage(), e);
			throw e;
		} catch (IOException e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}

		return doc;
	}

}
