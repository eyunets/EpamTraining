package com.epam.training.task2.entity;

import java.io.IOException;

import com.epam.training.task2.service.FileHandler;

public interface TextFragment {

	public void write(String outputPath) throws IOException;
}
