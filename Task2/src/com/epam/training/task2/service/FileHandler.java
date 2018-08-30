package com.epam.training.task2.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class FileHandler {

	private static final Logger LOGGER = Logger.getRootLogger();

	public static String read(String fileName) {
		StringBuilder stringBuilder = new StringBuilder();
		try (InputStream inputStream = new FileInputStream(fileName)) {
			String data = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			while ((data = reader.readLine()) != null) {
				stringBuilder.append(data);
				stringBuilder.append("\n");
			}
			reader.close();
		} catch (Exception e) {
			LOGGER.error("Error while reading " + fileName, e);
		}
		return stringBuilder.toString();
	}

	public static void write(String s, String filePath) throws IOException {
		LOGGER.debug("Writing data to " + filePath);
		FileWriter writer = new FileWriter(filePath, true);
		BufferedWriter bufWriter = new BufferedWriter(writer);
		bufWriter.write(s);
		bufWriter.close();
	}
}
