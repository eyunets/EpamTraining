package com.epam.training.task1.service;

import java.util.List;

import com.epam.training.task1.entity.Ammunition;

public class AmmunitionSorter {

	public static void sortByWeight(List<Ammunition> ammunition) {
		ammunition.sort((ammunition1, ammunition2) -> (int) (ammunition1.getWeight() - ammunition2.getWeight()));
	}

	public static void sortByPrice(List<Ammunition> ammunition) {
		ammunition.sort((ammunition1, ammunition2) -> (int) (ammunition1.getPrice() - ammunition2.getPrice()));
	}

}
