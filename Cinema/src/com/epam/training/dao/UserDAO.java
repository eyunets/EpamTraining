package com.epam.training.dao;

import java.util.List;

import com.epam.training.dao.exception.DAOException;
import com.epam.training.entity.User;

public interface UserDAO extends DAO<User> {

	List<User> getByEmail(String email) throws DAOException;
	List<User> getByType(String type) throws DAOException;
}
