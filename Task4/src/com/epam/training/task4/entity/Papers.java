package com.epam.training.task4.entity;

import java.util.ArrayList;
import java.util.List;

public class Papers {

	public List<Paper> paperList = new ArrayList<>();

	public void add(Paper paper) {
		this.paperList.add(paper);
	}

	@Override
	public String toString() {
		return "Papers [paperList=" + paperList + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((paperList == null) ? 0 : paperList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Papers other = (Papers) obj;
		if (paperList == null) {
			if (other.paperList != null)
				return false;
		} else if (!paperList.equals(other.paperList))
			return false;
		return true;
	}
	

}
