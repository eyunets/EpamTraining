package com.epam.training.task4.entity;

import java.util.ArrayList;
import java.util.List;

public class Papers {

	private List<Paper> paperList = new ArrayList<>();

	public void add(Paper paper) {
		this.paperList.add(paper);
	}

	@Override
	public String toString() {
		return "Papers [paperList=" + paperList + "]";
	}

}
