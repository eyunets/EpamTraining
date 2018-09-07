package com.epam.training.controller;

import java.time.LocalDate;

import com.epam.training.dao.FilmDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.dao.impl.FilmDAOImpl;
import com.epam.training.db.DBResourceManager;
import com.epam.training.entity.Film;

public class Main {
	public static void main(String[] args) {
		FilmDAO filmDAO = FilmDAOImpl.getInstance();
		Film film = new Film();
		film.setDescription("Description");
		film.setGenre("Action");
		film.setName("Superman");
		film.setPrice(10);
		film.setRating(7.8f);
		film.setYear(LocalDate.now());
		System.out.println(DBResourceManager.bundle);
		try {
			filmDAO.save(film);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
