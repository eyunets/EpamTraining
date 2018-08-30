package com.epam.training.task4.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class SchemaValidator {
	private final static Logger LOG = Logger.getLogger(SchemaValidator.class);

	static String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
	static SchemaFactory factory = SchemaFactory.newInstance(language);

	public static void validate(String filePath, String schemaPath) {
		try {
			Schema schema = factory.newSchema(new File(schemaPath));
			Validator validator = schema.newValidator();
			Source source = new StreamSource(new File(filePath));
			validator.validate(source);
			LOG.info("file is valid");
		} catch (SAXException ex) {
			LOG.error("Validation failed", ex);
		} catch (IOException ex) {
			LOG.error("IO exception", ex);
		}

	}
}
