package com.epam.training.task2.runner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.epam.training.task2.entity.CompositeTextFragment;
import com.epam.training.task2.service.FileHandler;
import com.epam.training.task2.service.ListingParser;
import com.epam.training.task2.service.ParagraphParser;
import com.epam.training.task2.service.Parser;
import com.epam.training.task2.service.SentenceParser;

public class Runner {

	private static final Logger LOGGER = Logger.getRootLogger();

	private static final String FILE_INPUT_PATH = "C:\\Users\\Yauhenii\\git\\repository\\Task2\\resources\\text.txt";
	private static final String FILE_OUTPUT_PATH = "C:\\Users\\Yauhenii\\git\\repository\\Task2\\resources\\output.txt";

	public static void main(String[] args) {

		LOGGER.debug("Start application.");
		Scanner input = new Scanner(System.in);
		String filePath = "";

		CompositeTextFragment text = new CompositeTextFragment();
		Parser parser = new ListingParser();
		parser.linkWith(new ParagraphParser()).linkWith(new SentenceParser());
		try {
			String data = FileHandler.read(FILE_INPUT_PATH);
			text = (CompositeTextFragment) parser.parsing(data);
			text.write(FILE_OUTPUT_PATH);
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
		} catch (Exception e) {

		}

		try {
			text.write(FILE_OUTPUT_PATH);
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
		}
	}

}
