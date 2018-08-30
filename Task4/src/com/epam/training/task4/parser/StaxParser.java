package com.epam.training.task4.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.epam.training.task4.entity.Paper;
import com.epam.training.task4.entity.PaperType;
import com.epam.training.task4.entity.Papers;

public class StaxParser {
	private Papers papers = new Papers();
	private Paper paper;
	private String thisElement;

	public void parse(File file) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
			InputStream input = new FileInputStream(file);
			XMLStreamReader xmlReader = factory.createXMLStreamReader(input);
			process(xmlReader);
	}

	public Papers getResult() {
		return papers;
	}

	private void process(XMLStreamReader xmlReader) throws XMLStreamException {
		while (xmlReader.hasNext()) {
			int type = xmlReader.next();
			switch (type) {
			case XMLStreamConstants.START_ELEMENT:
				thisElement = xmlReader.getLocalName();
				if (thisElement.equals("paper")) {
					paper = new Paper();
					papers.add(paper);
				}
				break;
			case XMLStreamConstants.CHARACTERS:
				String str = xmlReader.getText().trim();
				if (str.isEmpty()) {
					break;
				}
				switch (thisElement) {
				case "title":
					paper.setTitle(str);
					break;
				case "type":
					paper.setType(PaperType.valueOf(str.toUpperCase()));
					break;
				case "monthly":
					paper.setMonthly(Boolean.parseBoolean(str));
					break;
				case "color":
					paper.getChars().setColor(Boolean.parseBoolean(str));
					break;
				case "numberOfPage":
					paper.getChars().setNumberOfPage(Integer.parseInt(str));
					break;
				case "glossy":
					paper.getChars().setGlossy(Boolean.parseBoolean(str));
					break;
				case "subscriptionIndex":
					paper.getChars().setSubscriptionIndex(str);
					break;
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				// thisElement = null;
				break;
			}
		}

	}
}
