package com.epam.training.task2.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.epam.training.task2.entity.CompositeTextFragment;
import com.epam.training.task2.entity.Leaf;
import com.epam.training.task2.entity.TextFragment;

public class SentenceParser extends Parser {

	private static final String PATTERN = "([^.!?\\s]+)\\s*";
	
	private static final Logger LOGGER = Logger.getRootLogger();
	
	public SentenceParser() {}
	
	@Override
	public TextFragment parsing(String data) {
		LOGGER.debug("Sentence parsing.");
		CompositeTextFragment sentence = new CompositeTextFragment();
		
		Pattern expresion = Pattern.compile(PATTERN);
		Matcher match = expresion.matcher(data);
		
		while(match.find()) {
			sentence.add(new Leaf(match.group()));
		}
		
		return sentence;
	}

}
