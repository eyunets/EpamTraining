package com.epam.training.task4.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.epam.training.task4.entity.Chars;
import com.epam.training.task4.entity.Paper;
import com.epam.training.task4.entity.PaperType;
import com.epam.training.task4.entity.Papers;
import com.epam.training.task4.parser.SaxHandler;

import junit.framework.Assert;

public class XMLSaxParserTest {

	private File file;
	private SAXParserFactory factory;
	private SAXParser parser;
	private SaxHandler saxHandler;
	private Papers papers;

	@Before
	public void createPapers() {
		papers = new Papers();
		papers.add(new Paper("Magazine", PaperType.MAGAZINE, true, new Chars(true, 60, true, "220026")));
		papers.add(new Paper("Newspaper", PaperType.NEWSPAPER, true, new Chars(false, 10, false, "220000")));
		papers.add(new Paper("Around World", PaperType.MAGAZINE, true, new Chars(true, 90, true, "220026")));
		papers.add(new Paper("Soviet", PaperType.NEWSPAPER, true, new Chars(false, 12, false, "223400")));
		papers.add(new Paper("Discover", PaperType.MAGAZINE, true, new Chars(true, 120, true, "220326")));
		papers.add(new Paper("NY Times", PaperType.NEWSPAPER, true, new Chars(false, 20, false, "243700")));
		papers.add(new Paper("Inside", PaperType.MAGAZINE, false, new Chars(true, 40, true, "220026")));
		papers.add(new Paper("Independent", PaperType.NEWSPAPER, true, new Chars(false, 10, false, "220090")));
		papers.add(new Paper("Booklet", PaperType.BOOKLET, false, new Chars(false, 10, false, "222026")));
	}

	@Test
	public void parseSAX() {
		file = new File("papers.xml");
		factory = SAXParserFactory.newInstance();
		parser = null;
		saxHandler = new SaxHandler();

		try {
			parser = factory.newSAXParser();
			parser.parse(file, saxHandler);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(papers.equals(saxHandler.getResult()));
	}
}
