package com.epam.training.task4.controller;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.epam.training.task4.parser.DomParser;
import com.epam.training.task4.parser.SaxHandler;
import com.epam.training.task4.parser.SchemaValidator;

public class Runner {

	public static final Logger LOG = Logger.getLogger(Runner.class.toString());

	public static void main(String args[]) throws Exception {
		//VALIDATOR
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
		}
		SaxHandler saxp = new SaxHandler();
		try {
			parser.parse(file, saxp);
		} catch (IOException e) {
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
			System.out.println();
			LOG.info("DOM parser result:\n" + domParser.parse(doc));
		} catch (Exception e) {
			LOG.error("Dom exception", e);
		}
	}

}