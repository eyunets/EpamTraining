package com.epam.training.task2.entity;

import org.apache.log4j.Logger;

import com.epam.training.task2.service.FileHandler;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CompositeTextFragment implements TextFragment {
	private List<TextFragment> children = new ArrayList<>();

	private static final Logger LOGGER = Logger.getLogger(CompositeTextFragment.class);

	public void add(TextFragment child) {
		LOGGER.debug("Add new child.");
		children.add(child);
	}

	public void add(TextFragment... children) {
		LOGGER.debug("Add new children");
		this.children.addAll(Arrays.asList(children));
	}

	public List<TextFragment> getChildren() {
		return children;
	}

	@Override
	public void write(String outputPath) throws IOException {
		for (TextFragment fragment : children) {
			fragment.write(outputPath);
		}
	}
}
