package com.epam.training.task1.service;

import java.util.Random;

import com.epam.training.task1.entity.Ammunition;
import com.epam.training.task1.entity.AmmunitionType;
import com.epam.training.task1.entity.Motorcyclist;

public class MotorcyclistOutfitter {
	private static Random random = new Random();

	private static final int AMMUNITION_COUNT = 3;
	private static final int AMMUNITION_TYPE_COUNT = 3;

	public static Motorcyclist createMotorcyclist() {
		Motorcyclist motorcyclist = new Motorcyclist();
		for (int i = 0; i < AMMUNITION_COUNT; i++) {
			motorcyclist.addAmmunition(createAmmunition());
		}
		return motorcyclist;
	}

	public static Ammunition createAmmunition() {
		switch (random.nextInt(AMMUNITION_TYPE_COUNT)) {
		case 0:
			return new Ammunition(AmmunitionType.HELMET, 3, 150);
		case 1:
			return new Ammunition(AmmunitionType.GLOVES, 1, 80);
		default:
			return new Ammunition(AmmunitionType.SUIT, 5, 500);
		}
	}
}
