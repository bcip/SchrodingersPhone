package com.github.bcip.schrodingersphone.server;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.github.bcip.schrodingersphone.Predictor;
import com.github.bcip.schrodingersphone.network.SocketServer;

public class Server {
	public Server(String driver, String databaseURL, Predictor pd) {
		this(driver, databaseURL, pd, DEFAULT_PORT);
	}

	public Server(String driver, String databaseURL, Predictor pd, int port) {
		this(driver, databaseURL, pd, port, DEFAULT_NUM_THREADS);
	}

	public Server(String driver, String databaseURL, Predictor pd, int port, int numThreads) {
		this.driver = driver;
		this.databaseURL = databaseURL;
		this.pd = pd;
		this.port = port;
		this.numThreads = numThreads;
	}

	public void start() throws Exception {
		connectDatabase();
		dm = new DataManager(conn);
		initDatabase();
		startServer();
		infoStream.println("----------------------------------------");
		infoStream.println("SchrodingersPhone server started.");
		infoStream.println("----------------------------------------");
	}

	public void stop() throws SQLException {
		stopServer();
		disconnectDatabase();
		infoStream.println("----------------------------------------");
		infoStream.println("SchrodingersPhone server stopped.");
		infoStream.println("----------------------------------------");
	}

	protected void connectDatabase() throws SQLException,
			ClassNotFoundException {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(databaseURL + ";create=true");
		} catch (SQLException e) {
			infoStream.println("Failed to connect to database\"" + databaseURL
					+ "\".");
			e.printStackTrace(logStream);
			throw e;
		} catch (ClassNotFoundException e) {
			infoStream.println("Driver class name not found.");
			e.printStackTrace(logStream);
			throw e;
		}
		infoStream.println("Connected to database \"" + databaseURL + "\".");
	}

	/*
	 * check tables, create if not exist
	 */
	protected void initDatabase() throws SQLException {
		dm.initDatabase();
	}

	protected void startServer() throws IOException, SQLException {
		server = new SocketServer(port);
		server.addHandler(new MultithreadServerHandler(this, numThreads));
		server.connect();
		server.start();
		infoStream.println("SocketServer started.");
	}

	protected void stopServer() {
		server.stop();
		infoStream.println("SocketServer stopped.");
	}

	protected void disconnectDatabase() {
		try {
			conn.close();
		} catch (SQLException e) {
			infoStream.println("Failed to close the connection to database \""
					+ databaseURL + "\".");
			e.printStackTrace(logStream);
		}
		infoStream.println("Connection to database \"" + databaseURL
				+ "\" was closed.");

		if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
			boolean gotSQLExc = false;
			try {
				DriverManager.getConnection("jdbc:derby:;shutdown=true");
			} catch (SQLException e) {
				if (e.getSQLState().equals("XJ015")) {
					gotSQLExc = true;
				}
				else{
					e.printStackTrace(logStream);
				}
			}
			if (gotSQLExc) {
				infoStream.println("Database \"" + databaseURL
						+ "\" shutted down normally.");
			} else {
				infoStream.println("Database \"" + databaseURL
						+ "\" did not shut down normally.");
			}
		}
	}

	protected Connection conn = null;
	protected SocketServer server = null;
	protected DataManager dm = null;
	protected Predictor pd = null;

	protected String driver;
	protected String databaseURL;
	protected int port;
	protected int numThreads;

	protected static PrintStream infoStream = System.out;
	protected static PrintStream logStream = System.out;
	public static final int DEFAULT_PORT = 5641;
	public static final int DEFAULT_NUM_THREADS = 100;
}
