package com.epam.training.task4.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.epam.training.task4.parser.DomParser;
import com.epam.training.task4.parser.SaxHandler;
import com.epam.training.task4.parser.SchemaValidator;
import com.epam.training.task4.parser.StaxParser;

public class Runner {

	public static final Logger LOG = Logger.getLogger(Runner.class.toString());

	public static void main(String args[]) {
		// VALIDATOR
		String filePath = "papers.xml";
		String schemaPath = "papers.xsd";
		SchemaValidator.validate(filePath, schemaPath);

		File file = new File("papers.xml");
		// SAX
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = null;
		try {
			parser = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SaxHandler saxp = new SaxHandler();
		try {
			parser.parse(file, saxp);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info("Sax parser result : \n" + saxp.getResult());
		// DOM
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			DomParser domParser = new DomParser();
			LOG.info("DOM parser result:\n" + domParser.parse(doc));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		// STAX

		try {
			StaxParser staxParser = new StaxParser();
			staxParser.parse(file);
			LOG.info("STAX parser result:\n" + staxParser.getResult());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

}