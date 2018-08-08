package com.epam.training.task1.entity;

public enum AmmunitionType {
	HELMET, GLOVES, SUIT;

	public static AmmunitionType getType(String type) {
		switch (type) {
		case "HELMET":
			return HELMET;
		case "GLOVES":
			return GLOVES;
		case "SUIT":
			return SUIT;
		}
		return null;
	}
}
