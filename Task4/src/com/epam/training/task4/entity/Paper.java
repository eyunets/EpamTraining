package com.epam.training.task4.entity;

public class Paper {
	
	private String title;
	private  PaperType paperType;
	private boolean monthly;
	private Chars chars;

	public Paper() {
		this.chars = new Chars();
	}

	public Paper(String title, PaperType paperType, boolean monthly, Chars chars) {
		super();
		this.title = title;
		this.paperType = paperType;
		this.monthly = monthly;
		this.chars = chars;
	}

	public Chars getChars() {
		return this.chars;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(PaperType type) {
		this.paperType = type;
	}

	public void setMonthly(boolean monthly) {
		this.monthly = monthly;
	}

	@Override
	public String toString() {
		return "Paper [title=" + title + ", type=" + paperType + ", monthly=" + monthly + ", chars=" + chars + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chars == null) ? 0 : chars.hashCode());
		result = prime * result + (monthly ? 1231 : 1237);
		result = prime * result + ((paperType == null) ? 0 : paperType.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Paper other = (Paper) obj;
		if (chars == null) {
			if (other.chars != null)
				return false;
		} else if (!chars.equals(other.chars))
			return false;
		if (monthly != other.monthly)
			return false;
		if (paperType != other.paperType)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	
	
}
