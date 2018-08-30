package com.epam.training.task4.entity;

public class Chars {
	private boolean color;
	private int numberOfPage;
	private boolean glossy;
	private String subscriptionIndex;
	
	public Chars() {};
	
	public Chars(boolean color, int numberOfPage, boolean glossy, String subscriptionIndex) {
		super();
		this.color = color;
		this.numberOfPage = numberOfPage;
		this.glossy = glossy;
		this.subscriptionIndex = subscriptionIndex;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public void setNumberOfPage(int numberOfPage) {
		this.numberOfPage = numberOfPage;
	}

	public void setGlossy(boolean glossy) {
		this.glossy = glossy;
	}

	public void setSubscriptionIndex(String subscriptionIndex) {
		this.subscriptionIndex = subscriptionIndex;
	}

	@Override
	public String toString() {
		return "Chars [color=" + color + ", numberOfPage=" + numberOfPage + ", glossy=" + glossy
				+ ", subscriptionIndex=" + subscriptionIndex + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (color ? 1231 : 1237);
		result = prime * result + (glossy ? 1231 : 1237);
		result = prime * result + numberOfPage;
		result = prime * result + ((subscriptionIndex == null) ? 0 : subscriptionIndex.hashCode());
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
		Chars other = (Chars) obj;
		if (color != other.color)
			return false;
		if (glossy != other.glossy)
			return false;
		if (numberOfPage != other.numberOfPage)
			return false;
		if (subscriptionIndex == null) {
			if (other.subscriptionIndex != null)
				return false;
		} else if (!subscriptionIndex.equals(other.subscriptionIndex))
			return false;
		return true;
	}

	
}
