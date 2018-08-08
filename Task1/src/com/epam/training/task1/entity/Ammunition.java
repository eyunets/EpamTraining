package com.epam.training.task1.entity;

public class Ammunition {
	private double price;
	private double weight;
	private AmmunitionType ammunitionType;

	public Ammunition() {
	}

	public Ammunition(AmmunitionType ammunitionType, double price, double weight) {
		this.ammunitionType = ammunitionType;
		this.price = price;
		this.weight = weight;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	};

	public AmmunitionType getAmmunitionType() {
		return ammunitionType;
	}

	@Override
	public String toString() {
		return "Ammunition [price=" + price + ", weight=" + weight + ", ammunitionType=" + ammunitionType + "]";
	}
}
