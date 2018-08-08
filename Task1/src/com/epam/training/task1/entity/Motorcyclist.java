package com.epam.training.task1.entity;

import java.util.ArrayList;
import java.util.List;

public class Motorcyclist {
	private List<Ammunition> ammunition;
	
	public Motorcyclist() {
		ammunition = new ArrayList<>();
	}
	
	public void addAmmunition(Ammunition ammunitionPart) {
		this.ammunition.add(ammunitionPart);
	}

	public List<Ammunition> getAmmunition() {
		return ammunition;
	}

	public void setAmmunition(List<Ammunition> ammunition) {
		this.ammunition = ammunition;
	}

}
