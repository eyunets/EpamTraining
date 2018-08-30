package com.epam.training.task4.parser;

import org.apache.log4j.Logger;
import org.xml.sax.helpers.DefaultHandler;

import com.epam.training.task4.entity.Paper;
import com.epam.training.task4.entity.PaperType;
import com.epam.training.task4.entity.Papers;

import org.xml.sax.*;

public class SaxHandler extends DefaultHandler {

	public static final Logger LOG = Logger.getLogger(SaxHandler.class.toString());

	private Papers papers = new Papers();
	private Paper paper;
	private String thisElement;

	public SaxHandler() {
		paper = new Paper();
	}

	public Papers getResult() {
		return papers;
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		thisElement = qName;
		if (thisElement == "paper") {
			paper = new Paper();
			papers.add(paper);
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		thisElement = null;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String str = new String(ch, start, length).trim();
		if (str.isEmpty()) {
			return;
		}
		if (thisElement != null) {
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
		}
	}
}