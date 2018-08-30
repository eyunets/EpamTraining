package com.epam.training.task4.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.epam.training.task4.entity.Paper;
import com.epam.training.task4.entity.PaperType;
import com.epam.training.task4.entity.Papers;

public class DomParser {
	private Paper paper;
	private Papers papers = new Papers();

	public DomParser() {
	}

	public Papers parse(Document input) {
		parseNode(input.getDocumentElement());
		return papers;
	}

	private Paper parseNode(Node node) {

		if (node.getNodeName().equals("#text"))
			return null;
		if (getElementContent(node) != null && !(getElementContent(node).equals(""))) {
			switch (node.getNodeName()) {
			case "title":
				paper = new Paper();
				papers.add(paper);
				paper.setTitle(getElementContent(node));
				break;
			case "type":
				paper.setType((PaperType.valueOf(getElementContent(node).toUpperCase())));
				break;
			case "monthly":
				paper.setMonthly(Boolean.parseBoolean(getElementContent(node)));
				break;
			case "color":
				paper.getChars().setColor(Boolean.parseBoolean(getElementContent(node)));
				break;
			case "numberOfPage":
				paper.getChars().setNumberOfPage(Integer.parseInt(getElementContent(node)));
				break;
			case "glossy":
				paper.getChars().setGlossy(Boolean.parseBoolean(getElementContent(node)));
				break;
			case "subscriptionIndex":
				paper.getChars().setSubscriptionIndex(getElementContent(node));
				break;
			}
		}

		NodeList nodeList = node.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			parseNode(nodeList.item(i));
		}
		return paper;
	}

	private String getElementContent(Node node) {

		Node contentNode = node.getFirstChild();
		if (contentNode != null)
			if (contentNode.getNodeName().equals("#text")) {
				String value = contentNode.getNodeValue();
				if (value != null)
					return value.trim();
			}
		return null;
	}
}