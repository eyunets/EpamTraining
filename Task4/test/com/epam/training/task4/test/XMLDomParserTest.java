package com.epam.training.task4.test;

import static org.junit.Assert.*;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import com.epam.training.task4.entity.Chars;
import com.epam.training.task4.entity.Paper;
import com.epam.training.task4.entity.PaperType;
import com.epam.training.task4.entity.Papers;
import com.epam.training.task4.parser.DomParser;
import com.epam.training.task4.parser.SaxHandler;

import junit.framework.Assert;

public class XMLDomParserTest {

	private File file;
	private Papers papers;
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;
	private DomParser domParser;

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
	public void test() {
		try {
			file = new File("papers.xml");
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			domParser = new DomParser();
			Assert.assertTrue(papers.equals(domParser.parse(doc)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
