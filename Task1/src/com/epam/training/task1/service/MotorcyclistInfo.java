package com.epam.training.task1.service;

import java.util.List;

import com.epam.training.task1.entity.Ammunition;

public class MotorcyclistInfo {

	public static double getPrice(List<Ammunition> ammunition) {
		double sum = 0.0;
		for (Ammunition ammunitionPart : ammunition) {
			sum += ammunitionPart.getPrice();
		}
		return sum;
	}
}
