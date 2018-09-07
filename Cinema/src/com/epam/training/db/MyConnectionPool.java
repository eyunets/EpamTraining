package com.epam.training.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.epam.training.db.exception.ConnectionPoolException;


public final class MyConnectionPool {

	public static final Logger logger = Logger.getLogger(MyConnectionPool.class.toString());

	private static MyConnectionPool instance;

	private Queue<Connection> freeConnections;
	private Queue<Connection> busyConnections;

	private String driverName;
	private String url;
	private String user;
	private String password;
	private int poolSize;

	public synchronized static MyConnectionPool getInstance() throws ConnectionPoolException {
		if (instance == null) {
			instance = new MyConnectionPool();
		}
		return instance;
	}

	private MyConnectionPool() throws ConnectionPoolException {
		freeConnections = new LinkedList<Connection>();
		busyConnections = new LinkedList<Connection>();
		DBResourceManager manager = DBResourceManager.getInstance();

		this.driverName = manager.getValue(DBConfiguration.DB_DRIVER);
		this.url = manager.getValue(DBConfiguration.DB_URL);
		this.user = manager.getValue(DBConfiguration.DB_USER);
		this.password = manager.getValue(DBConfiguration.DB_PASSWORD);
		
		/*this.driverName = "com.mysql.jdbc.Driver";
		this.url = "jdbc:mysql://localhost:3306/cinema?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
		this.user = "root";
		this.password = "4242";
		this.poolSize = 10 ;*/
		
		try {
			this.poolSize = Integer.parseInt(manager.getValue(DBConfiguration.DB_POOL_SIZE));
			Class.forName(driverName);
		} catch (NumberFormatException e) {
			poolSize = 10;
		} catch (ClassNotFoundException e) {
			throw new ConnectionPoolException("SQLException happened in ConnectionPool", e);
		}
	}

	public synchronized Connection getConnection() throws ConnectionPoolException {
		if (freeConnections.isEmpty() && busyConnections.isEmpty()) {
			fillEmptyConnectionList();
		} else if (freeConnections.isEmpty() && !busyConnections.isEmpty()) {
			throw new ConnectionPoolException("No free connection available");
		}
		Connection con = freeConnections.poll();
		busyConnections.add(con);
		return con;
	}

	private void fillEmptyConnectionList() throws ConnectionPoolException {
		try {
			Connection con = null;
			for (int i = 0; i < poolSize; i++) {
				con = DriverManager.getConnection(url, user, password);
				freeConnections.add(con);

			}
		} catch (SQLException e) {
			throw new ConnectionPoolException("SQLException happened in ConnectionPool", e);
		}
	}

	private void addOneNewConnection() throws ConnectionPoolException {
		try {
			Connection con = DriverManager.getConnection(url, user, password);
			freeConnections.add(con);
		} catch (SQLException e) {
			throw new ConnectionPoolException("SQLException happened in ConnectionPool", e);
		}

	}

	public void closeConnection(Connection con) {
		try {
			freeConnection(con);
		} catch (SQLException e) {
			logger.error("Connection is not closed and not returned to pool", e);
		}
	}

	private synchronized void freeConnection(Connection con) throws SQLException {
		if (con.isClosed()) {
			throw new SQLException("Impossible to close closed connection.");
		}
		if (!con.getAutoCommit()) {
			con.commit();
		}
		if (!busyConnections.remove(con)) {
			throw new SQLException("No such connection found in the connection pool");
		}

		if (!freeConnections.offer(con)) {
			throw new SQLException("Error allocating connection in the pool. No space currently available");
		}
	}
}