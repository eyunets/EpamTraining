package com.epam.training.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.dao.UserDAO;
import com.epam.training.dao.auth.MyEncoder;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.db.MyConnectionPool;
import com.epam.training.db.exception.ConnectionPoolException;
import com.epam.training.entity.User;
import lombok.extern.log4j.Log4j;

@Log4j
public class UserDAOImpl implements UserDAO {

	private static final String SAVE_USER_QUERY = "INSERT INTO USER ( NAME, SURNAME, PASSWORD, TYPE, EMAIL) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_USER_QUERY = "UPDATE USER SET NAME=?, SURNAME=?, SECOND_NAME=?, PASSWORD=?, TYPE=?, EMAIL=? WHERE USER_ID=?";
	private static final String GET_USER_QUERY = "SELECT * FROM USER WHERE USER_ID=?";
	private static final String GET_ALL_USERS_QUERY = "SELECT * FROM USER";
	private static final String GET_USER_BY_EMAIL_QUERY = "SELECT * FROM USER WHERE EMAIL=?";
	private static final String GET_USER_BY_TYPE_QUERY = "SELECT * FROM USER WHERE TYPE=?";
	private static final String DELETE_USER_QUERY = "DELETE FROM USER WHERE USER_ID=?";

	private static volatile UserDAO INSTANCE = null;
	private PreparedStatement psSave;
	private PreparedStatement psUpdate;
	private PreparedStatement psGet;
	private PreparedStatement psGetByEmail;
	private PreparedStatement psGetByType;
	private PreparedStatement psGetAll;
	private PreparedStatement psDelete;

	private UserDAOImpl() {
	}

	public static UserDAO getInstance() {
		UserDAO userDAO = INSTANCE;
		if (userDAO == null) {
			synchronized (UserDAOImpl.class) {
				userDAO = INSTANCE;
				if (userDAO == null) {
					INSTANCE = userDAO = new UserDAOImpl();
				}
			}
		}

		return userDAO;
	}

	private static void close(ResultSet rs) throws SQLException {
		if (rs != null)
			rs.close();
	}
	@Override
	public User save(User user) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psSave = con.prepareStatement(SAVE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
			psSave.setString(1, user.getName());
			psSave.setString(2, user.getSurname());
			psSave.setString(3, MyEncoder.encode(user.getPassword()));
			psSave.setString(4, user.getType());
			psSave.setString(5, user.getEmail());
			psSave.executeUpdate();

			rs = psSave.getGeneratedKeys();
			if (rs.next()) {
				user.setId(rs.getInt(1));
			}
			return user;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL save problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				MyConnectionPool.getInstance().closeConnection(con);
			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}

	}
	@Override
	public User get(Serializable id) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGet = con.prepareStatement(GET_USER_QUERY);
			psGet.setInt(1, (int) id);
			psGet.executeQuery();
			rs = psGet.getResultSet();
			if (rs.next()) {
				return populateUser(rs);
			}
			return null;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL get problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				MyConnectionPool.getInstance().closeConnection(con);
			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}
	@Override
	public void update(User user) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psUpdate = con.prepareStatement(UPDATE_USER_QUERY);
			psUpdate.setString(1, user.getName());
			psUpdate.setString(2, user.getSurname());
			psUpdate.setString(3, MyEncoder.encode(user.getPassword()));
			psUpdate.setString(4, user.getType());
			psUpdate.setString(5, user.getEmail());
			psUpdate.setInt(6, user.getId());
			psUpdate.executeUpdate();
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL update problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				MyConnectionPool.getInstance().closeConnection(con);
			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}
	@Override
	public int delete(Serializable id) throws DAOException {
		Connection con = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psDelete = con.prepareStatement(DELETE_USER_QUERY);
			psDelete.setInt(1, (int) id);
			return psDelete.executeUpdate();
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL delete problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				MyConnectionPool.getInstance().closeConnection(con);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}
	@Override
	public List<User> getAll() throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGetAll = con.prepareStatement(GET_ALL_USERS_QUERY);
			List<User> list = new ArrayList<>();
			psGetAll.execute();
			rs = psGetAll.getResultSet();
			while (rs.next()) {
				list.add(populateUser(rs));
			}
			return list;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL update problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				MyConnectionPool.getInstance().closeConnection(con);

			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}
	@Override
	public List<User> getByEmail(String email) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGetByEmail = con.prepareStatement(GET_USER_BY_EMAIL_QUERY);
			List<User> list = new ArrayList<>();
			psGetByEmail.setString(1, email);
			psGetByEmail.execute();
			rs = psGetByEmail.getResultSet();
			while (rs.next()) {
				list.add(populateUser(rs));
			}
			return list;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL update problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				MyConnectionPool.getInstance().closeConnection(con);

			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}
	@Override
	public List<User> getByType(String type) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGetByType = con.prepareStatement(GET_USER_BY_TYPE_QUERY);
			List<User> list = new ArrayList<>();
			psGetByType.setString(1, type);
			psGetByType.execute();
			rs = psGetByType.getResultSet();
			while (rs.next()) {
				list.add(populateUser(rs));
			}

			return list;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL update problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection pool problem", e);
		} finally {
			try {
				close(rs);
				MyConnectionPool.getInstance().closeConnection(con);

			} catch (SQLException e) {
				log.error(e);
				throw new DAOException("ResultSet close problem", e);
			} catch (ConnectionPoolException e) {
				log.error(e);
				throw new DAOException("Connection close problem", e);

			}
		}
	}

	private User populateUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt(1));
		user.setName(rs.getString(2));
		user.setSurname(rs.getString(3));
		user.setPassword(rs.getString(4));
		user.setType(rs.getString(5));
		user.setEmail(rs.getString(6));
		return user;
	}
}
