package com.epam.training.task4.test;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epam.training.task4.entity.Chars;
import com.epam.training.task4.entity.Paper;
import com.epam.training.task4.entity.PaperType;
import com.epam.training.task4.entity.Papers;
import com.epam.training.task4.parser.StaxParser;

public class XMLStaxParserTest {

	private File file;
	private Papers papers;
	private StaxParser staxParser;

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
	public void parseSTAX() {
		try {
			file = new File("papers.xml");
			staxParser = new StaxParser();
			staxParser.parse(file);
			Assert.assertTrue(papers.equals(staxParser.getResult()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

}