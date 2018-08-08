package com.epam.training.task2.service;

import com.epam.training.task2.entity.Leaf;
import com.epam.training.task2.entity.TextFragment;

public abstract class Parser {

	private Parser next;

	public Parser linkWith(Parser next) {
		this.next = next;
		return next;
	}

	public abstract TextFragment parsing(String data);

	protected TextFragment parseNext(String data) {
		if (next == null) {
			return new Leaf(data);
		}
		return next.parsing(data);
	}

}
