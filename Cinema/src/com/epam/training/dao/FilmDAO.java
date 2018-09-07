package com.epam.training.dao;

import java.util.List;

import com.epam.training.dao.exception.DAOException;
import com.epam.training.entity.Film;

public interface FilmDAO extends DAO<Film> {

	List<Film> getByName(String name) throws DAOException;

	List<Film> getByGenre(String genre) throws DAOException;

}
