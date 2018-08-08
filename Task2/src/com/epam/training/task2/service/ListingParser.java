package com.epam.training.task2.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.epam.training.task2.entity.CompositeTextFragment;
import com.epam.training.task2.entity.Leaf;
import com.epam.training.task2.entity.TextFragment;

public class ListingParser extends Parser {

	private static final String PATTERN_START = "/\\*.*\\s+";
	private static final String PATTERN_END = "\\n}\\s+"; 
	private static final Logger LOGGER = Logger.getRootLogger();

	public ListingParser() {
	}

	@Override
	public TextFragment parsing(String data) {
		LOGGER.debug("Listing parsing.");
		CompositeTextFragment text = new CompositeTextFragment();

		Pattern expressionStart = Pattern.compile(PATTERN_START);
		Pattern expressionEnd = Pattern.compile(PATTERN_END);

		Matcher matchStart = expressionStart.matcher(data);
		Matcher matchEnd = expressionEnd.matcher(data);

		int end = 0;
		while (matchStart.find() && matchEnd.find()) {
			int start = data.indexOf(matchStart.group());

			for (String arg : data.substring(end, start).split("\\n")) {
				if (!arg.equals("")) {
					text.add(parseNext(arg));
				}
				text.add(new Leaf(System.lineSeparator()));
			}

			end = data.indexOf(matchEnd.group()) + matchEnd.group().length();
			text.add(new Leaf(data.substring(start, end)));
			data = data.substring(end);
			end = 0;

		}

		for (String arg : data.substring(end).split("\\n")) {
			if (!arg.equals("")) {
				text.add(parseNext(arg));
			}
			text.add(new Leaf(System.lineSeparator()));
		}

		return text;
	}

}
