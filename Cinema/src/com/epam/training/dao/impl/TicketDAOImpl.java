package com.epam.training.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.dao.TicketDAO;
import com.epam.training.dao.exception.DAOException;
import com.epam.training.db.MyConnectionPool;
import com.epam.training.db.exception.ConnectionPoolException;
import com.epam.training.entity.Film;
import com.epam.training.entity.Ticket;
import com.epam.training.entity.User;

import lombok.extern.log4j.Log4j;

@Log4j
public class TicketDAOImpl extends AbstractDAO implements TicketDAO {
	private static final String SAVE_TICKET_QUERY = "INSERT INTO TICKET (USER_ID, FILM_ID, DATE) VALUES (?, ?, ?)";
	private static final String UPDATE_TICKET_QUERY = "UPDATE TICKET SET USER_ID=?, FILM_ID=?, DATE=? WHERE TICKET_ID=?";
	private static final String GET_TICKET_QUERY = "SELECT * FROM TICKET WHERE TICKET_ID=?";
	private static final String GET_ALL_TICKETS_QUERY = "SELECT * FROM TICKET";
	private static final String GET_TICKET_BY_USER_ID_QUERY = "SELECT * FROM TICKET WHERE USER_ID=?";
	private static final String GET_TICKET_BY_FILM_ID_QUERY = "SELECT * FROM TICKET WHERE FILM_ID=?";
	private static final String DELETE_TICKET_QUERY = "DELETE FROM TICKET WHERE TICKET_ID=?";
	private static volatile TicketDAO INSTANCE = null;
	private PreparedStatement psSave;
	private PreparedStatement psUpdate;
	private PreparedStatement psGet;
	private PreparedStatement psGetByUserID;
	private PreparedStatement psGetByFilmID;
	private PreparedStatement psGetAll;
	private PreparedStatement psDelete;

	private TicketDAOImpl() {
	}

	public static TicketDAO getInstance() {
		TicketDAO ticketDAO = INSTANCE;
		if (ticketDAO == null) {
			synchronized (TicketDAOImpl.class) {
				ticketDAO = INSTANCE;
				if (ticketDAO == null) {
					INSTANCE = ticketDAO = new TicketDAOImpl();
				}
			}
		}
		return ticketDAO;
	}

	private static void close(ResultSet rs) throws SQLException {
		if (rs != null)
			rs.close();
	}

	@Override
	public Ticket save(Ticket ticket) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psSave = con.prepareStatement(SAVE_TICKET_QUERY, Statement.RETURN_GENERATED_KEYS);
			psSave.setInt(1, ticket.getUserId());
			psSave.setInt(2, ticket.getFilmId());
			psSave.setDate(3, toSQLDate(ticket.getDate()));
			psSave.executeUpdate();
			rs = psSave.getGeneratedKeys();
			if (rs.next()) {
				ticket.setId(rs.getInt(1));
			}
			return ticket;
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
	public Ticket get(Serializable id) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGet = con.prepareStatement(GET_TICKET_QUERY);
			psGet.setInt(1, (int) id);
			psGet.executeQuery();
			rs = psGet.getResultSet();
			if (rs.next()) {
				return populateTicket(rs);
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
	public void update(Ticket ticket) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psUpdate = con.prepareStatement(UPDATE_TICKET_QUERY);
			psSave.setInt(1, ticket.getUserId());
			psSave.setInt(2, ticket.getFilmId());
			psSave.setDate(3, toSQLDate(ticket.getDate()));
			psUpdate.setInt(4, ticket.getId());
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
			psDelete = con.prepareStatement(DELETE_TICKET_QUERY);
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
	public List<Ticket> getAll() throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGetAll = con.prepareStatement(GET_ALL_TICKETS_QUERY);
			List<Ticket> list = new ArrayList<>();
			psGetAll.execute();
			rs = psGetAll.getResultSet();
			while (rs.next()) {
				list.add(populateTicket(rs));
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
	public List<Ticket> getByUser(User user) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGetByUserID = con.prepareStatement(GET_TICKET_BY_USER_ID_QUERY);
			List<Ticket> list = new ArrayList<>();
			psGetByUserID.setInt(1, user.getId());
			psGetByUserID.execute();
			rs = psGetByUserID.getResultSet();
			while (rs.next()) {
				list.add(populateTicket(rs));
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
	public List<Ticket> getByFilm(Film film) throws DAOException {
		Connection con = null;
		ResultSet rs = null;
		try {
			con = MyConnectionPool.getInstance().getConnection();
			psGetByFilmID = con.prepareStatement(GET_TICKET_BY_FILM_ID_QUERY);
			List<Ticket> list = new ArrayList<>();
			psGetByFilmID.setInt(1, film.getId());
			psGetByFilmID.execute();
			rs = psGetByFilmID.getResultSet();
			while (rs.next()) {
				list.add(populateTicket(rs));
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

	private Ticket populateTicket(ResultSet rs) throws SQLException {
		Ticket ticket = new Ticket();
		ticket.setId(rs.getInt(1));
		ticket.setUserId(rs.getInt(2));
		ticket.setFilmId(rs.getInt(3));
		ticket.setDate(toLocalDate(rs.getDate(4)));
		return ticket;
	}
}
