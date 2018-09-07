package com.epam.training.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.dao.FilmDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.db.MyConnectionPool;
import com.epam.training.db.exception.ConnectionPoolException;
import com.epam.training.entity.Film;

import lombok.extern.log4j.Log4j;

@Log4j
public class FilmDAOImpl extends AbstractDAO implements FilmDAO {

	private static final String SAVE_FILM_QUERY = "INSERT INTO FILM (NAME, GENRE, YEAR, DESCRIPTION, RATING, PRICE) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_FILM_QUERY = "UPDATE FILM SET NAME=?, GENRE=?, YEAR=? ,DESCRIPTION=?, RATING=?, PRICE=? WHERE FILM_ID=?";
	private static final String GET_FILM_QUERY = "SELECT * FROM FILM WHERE FILM_ID=?";
	private static final String GET_ALL_FILM_QUERY = "SELECT * FROM FILM";
	private static final String GET_FILM_BY_NAME_QUERY = "SELECT * FROM FILM WHERE NAME=?";
	//private static final String getFilmByYearQuery = "SELECT * FROM FILM WHERE YEAR=?";
	private static final String GET_FILM_BY_GENRE_QUERY = "SELECT * FROM FILM WHERE GENRE=?";
	private static final String DELETE_FILM_QUERY = "DELETE FROM FILM WHERE FILM_ID=?";
	private static volatile FilmDAO INSTANCE = null;
	private PreparedStatement psSave;
	private PreparedStatement psUpdate;
	private PreparedStatement psGet;
	private PreparedStatement psGetByName;
	//private PreparedStatement psGetByYear;
	private PreparedStatement psGetByGenre;
	private PreparedStatement psGetAll;
	private PreparedStatement psDelete;

	private FilmDAOImpl() {
	}

	public static FilmDAO getInstance() {
		FilmDAO filmDAO = INSTANCE;
		if (filmDAO == null) {
			synchronized (FilmDAOImpl.class) {
				filmDAO = INSTANCE;
				if (filmDAO == null) {
					INSTANCE = filmDAO = new FilmDAOImpl();
				}
			}
		}

		return filmDAO;
	}

	private static void close(ResultSet rs) throws SQLException {
		if (rs != null)
			rs.close();
	}

	@Override
	public Film save(Film film) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psSave = con.prepareStatement(SAVE_FILM_QUERY, Statement.RETURN_GENERATED_KEYS);
			psSave.setString(1, film.getName());
			psSave.setString(2, film.getGenre());
			psSave.setDate(3, toSQLDate(film.getYear()));
			psSave.setString(4, film.getDescription());
			psSave.setFloat(5, film.getRating());
			psSave.setFloat(6, film.getPrice());
			psSave.executeUpdate();
			rs = psSave.getGeneratedKeys();
			if (rs.next()) {
				film.setId(rs.getInt(1));
			}
			return film;
		} catch (SQLException e) {
			log.error(e);
			throw new DAOException("SQL save problem", e);
		} catch (ConnectionPoolException e) {
			log.error(e);
			throw new DAOException("Connection con problem", e);
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
	public Film get(Serializable id) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGet = con.prepareStatement(GET_FILM_QUERY);
			psGet.setInt(1, (int) id);
			psGet.executeQuery();
			rs = psGet.getResultSet();
			if (rs.next()) {
				return populateFilm(rs);
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
	public void update(Film film) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psUpdate = con.prepareStatement(UPDATE_FILM_QUERY);
			psUpdate.setString(1, film.getName());
			psUpdate.setString(2, film.getGenre());
			psUpdate.setDate(3, toSQLDate(film.getYear()));
			psUpdate.setString(4, film.getDescription());
			psUpdate.setFloat(5, film.getRating());
			psUpdate.setFloat(6, film.getPrice());
			psUpdate.setInt(7, film.getId());
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
			psDelete = con.prepareStatement(DELETE_FILM_QUERY);
			psDelete.setInt(1, (int) id);
			return psDelete.executeUpdate();

		} catch (SQLException e) {
			throw new DAOException("Failure during SQL Delete Request execution", e);
		} catch (ConnectionPoolException e) {
			throw new DAOException("Failure during taking connection from ConnectionPool", e);
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
	public List<Film> getAll() throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGetAll = con.prepareStatement(GET_ALL_FILM_QUERY);
			List<Film> list = new ArrayList<>();
			psGetAll.execute();
			rs = psGetAll.getResultSet();
			while (rs.next()) {
				list.add(populateFilm(rs));
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
	public List<Film> getByName(String name) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGetByName = con.prepareStatement(GET_FILM_BY_NAME_QUERY);
			List<Film> list = new ArrayList<>();
			psGetByName.setString(1, name);
			psGetByName.execute();
			rs = psGetByName.getResultSet();
			while (rs.next()) {
				list.add(populateFilm(rs));
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
	public List<Film> getByGenre(String genre) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGetByGenre = con.prepareStatement(GET_FILM_BY_GENRE_QUERY);
			List<Film> list = new ArrayList<>();
			psGetByGenre.setString(1, genre);
			psGetByGenre.execute();
			rs = psGetByGenre.getResultSet();
			while (rs.next()) {
				list.add(populateFilm(rs));
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

	private Film populateFilm(ResultSet rs) throws SQLException {
		Film film = new Film();
		film.setId(rs.getInt(1));
		film.setName(rs.getString(2));
		film.setGenre(rs.getString(3));
		film.setYear(toLocalDate(rs.getDate(4)));
		film.setDescription(rs.getString(5));
		film.setRating(rs.getFloat(6));
		film.setPrice(rs.getFloat(7));
		return film;
	}

}
