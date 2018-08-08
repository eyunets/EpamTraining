package com.epam.training.task1.service;

import java.util.ArrayList;
import java.util.List;

import com.epam.training.task1.entity.Ammunition;

public class AmmunitionSearcher {
	public static List<Ammunition> find(List<Ammunition> ammunition, double PriceMin, double PriceMax) {
		List<Ammunition> list = new ArrayList<>();
		for (Ammunition ammunitionPart : ammunition) {
			if ((ammunitionPart.getPrice() >= PriceMin) && (ammunitionPart.getPrice() <= PriceMax)) {
				list.add(ammunitionPart);
			}
		}
		return list;
	}

}
