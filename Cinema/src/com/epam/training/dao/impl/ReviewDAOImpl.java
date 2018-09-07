package com.epam.training.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.dao.ReviewDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.db.MyConnectionPool;
import com.epam.training.db.exception.ConnectionPoolException;
import com.epam.training.entity.Film;
import com.epam.training.entity.Review;
import com.epam.training.entity.User;

import lombok.extern.log4j.Log4j;


@Log4j
public class ReviewDAOImpl extends AbstractDAO implements ReviewDAO {
	private static final String SAVE_REVIEW_QUERY = "INSERT INTO REVIEW (USER_ID, FILM_ID, DATE, MARK, TEXT) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_REVIEW_QUERY = "UPDATE REVIEW SET USER_ID=?, FILM_ID=?, DATE=?, MARK=?, TEXT = ? WHERE REVIEW_ID=?";
	private static final String GET_REVIEW_QUERY = "SELECT * FROM REVIEW WHERE REVIEW_ID=?";
	private static final String GET_ALL_REVIEWS_QUERY = "SELECT * FROM REVIEW";
	private static final String GET_REVIEW_BY_USER_ID_QUERY = "SELECT * FROM REVIEW WHERE USER_ID=?";
	private static final String GET_REVIEW_BY_FILM_ID_QUERY = "SELECT * FROM REVIEW WHERE FILM_ID=?";
	private static final String DELETE_REVIEW_QUERY = "DELETE FROM REVIEW WHERE REVIEW_ID=?";
	private static volatile ReviewDAO INSTANCE = null;
	private PreparedStatement psSave;
	private PreparedStatement psUpdate;
	private PreparedStatement psGet;
	private PreparedStatement psGetByUserID;
	private PreparedStatement psGetByFilmID;
	private PreparedStatement psGetAll;
	private PreparedStatement psDelete;

	private ReviewDAOImpl() {
	}

	public static ReviewDAO getInstance() {
		ReviewDAO reviewDAO = INSTANCE;
		if (reviewDAO == null) {
			synchronized (ReviewDAOImpl.class) {
				reviewDAO = INSTANCE;
				if (reviewDAO == null) {
					INSTANCE = reviewDAO = new ReviewDAOImpl();
				}
			}
		}
		return reviewDAO;
	}

	private static void close(ResultSet rs) throws SQLException {
		if (rs != null)
			rs.close();
	}


	@Override
	public Review save(Review review) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psSave = con.prepareStatement(SAVE_REVIEW_QUERY, Statement.RETURN_GENERATED_KEYS);
			psSave.setInt(1, review.getUserId());
			psSave.setInt(2, review.getFilmId());
			psSave.setDate(3, toSQLDate(review.getDate()));
			psSave.setInt(4, review.getMark());
			psSave.setString(5, review.getText());
			psSave.executeUpdate();
			rs = psSave.getGeneratedKeys();
			if (rs.next()) {
				review.setId(rs.getInt(1));
			}
			return review;
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
	public Review get(Serializable id) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGet = con.prepareStatement(GET_REVIEW_QUERY);
			psGet.setInt(1, (int) id);
			psGet.executeQuery();
			rs = psGet.getResultSet();
			if (rs.next()) {
				return populateReview(rs);
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
	public void update(Review review) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psUpdate = con.prepareStatement(UPDATE_REVIEW_QUERY);
			psSave.setInt(1, review.getUserId());
			psSave.setInt(2, review.getFilmId());
			psSave.setDate(3, toSQLDate(review.getDate()));
			psSave.setInt(4, review.getMark());
			psSave.setString(5, review.getText());
			psUpdate.setInt(6, review.getId());
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
			psDelete = con.prepareStatement(DELETE_REVIEW_QUERY);
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
	public List<Review> getAll() throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGetAll = con.prepareStatement(GET_ALL_REVIEWS_QUERY);
			List<Review> list = new ArrayList<>();
			psGetAll.execute();
			rs = psGetAll.getResultSet();
			while (rs.next()) {
				list.add(populateReview(rs));
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
	public List<Review> getByUser(User user) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGetByUserID = con.prepareStatement(GET_REVIEW_BY_USER_ID_QUERY);
			List<Review> list = new ArrayList<>();
			psGetByUserID.setInt(1, user.getId());
			psGetByUserID.execute();
			rs = psGetByUserID.getResultSet();
			while (rs.next()) {
				list.add(populateReview(rs));
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
	public List<Review> getByFilm(Film film) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGetByFilmID = con.prepareStatement(GET_REVIEW_BY_FILM_ID_QUERY);
			List<Review> list = new ArrayList<>();
			psGetByFilmID.setInt(1, film.getId());
			psGetByFilmID.execute();
			rs = psGetByFilmID.getResultSet();
			while (rs.next()) {
				list.add(populateReview(rs));
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

	private Review populateReview(ResultSet rs) throws SQLException {
		Review review = new Review();
		review.setId(rs.getInt(1));
		review.setUserId(rs.getInt(2));
		review.setFilmId(rs.getInt(3));
		review.setDate(toLocalDate(rs.getDate(4)));
		review.setMark(rs.getInt(5));
		review.setText(rs.getString(6));
		return review;
	}
}
