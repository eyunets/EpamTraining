package com.epam.training.dao;


import java.util.List;

import com.epam.training.dao.exception.DAOException;
import com.epam.training.entity.Film;
import com.epam.training.entity.Ticket;
import com.epam.training.entity.User;

public interface TicketDAO extends DAO<Ticket> {
	List<Ticket> getByUser(User user) throws DAOException;

	List<Ticket> getByFilm(Film film) throws DAOException;
}
