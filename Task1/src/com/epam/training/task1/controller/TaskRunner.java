package com.epam.training.task1.controller;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.epam.training.task1.entity.Motorcyclist;
import com.epam.training.task1.service.AmmunitionSearcher;
import com.epam.training.task1.service.AmmunitionSorter;
import com.epam.training.task1.service.MotorcyclistInfo;
import com.epam.training.task1.service.MotorcyclistOutfitter;

public class TaskRunner {

	private final static Logger LOGGER = Logger.getLogger("com.epam.training.task1.controller");

	public static void main(String[] args) {
		LOGGER.setResourceBundle(ResourceBundle.getBundle("log4j"));
		Motorcyclist motorcyclist = MotorcyclistOutfitter.createMotorcyclist();
		LOGGER.info("Motorcyclist outfit - " + motorcyclist.getAmmunition());
		LOGGER.info("Ammunition full price - " + MotorcyclistInfo.getPrice(motorcyclist.getAmmunition()));
		AmmunitionSorter.sortByPrice(motorcyclist.getAmmunition());
		LOGGER.info("Sorted by price - " + motorcyclist.getAmmunition());
		AmmunitionSorter.sortByWeight(motorcyclist.getAmmunition());
		LOGGER.info("Sorted by weight - " + motorcyclist.getAmmunition());
		LOGGER.info(
				"Ammunition with price between 1-2 - " + AmmunitionSearcher.find(motorcyclist.getAmmunition(), 1, 2));

	}

}
