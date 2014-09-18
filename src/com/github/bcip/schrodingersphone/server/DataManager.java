package com.github.bcip.schrodingersphone.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.github.bcip.schrodingersphone.Record;

public class DataManager {
	public DataManager(Connection conn) {
		this.conn = conn;
		// TODO;
	}

	public void initDatabase() throws SQLException {
		try {
			Statement s = conn.createStatement();
			s.execute("UPDATE user_record SET username = \'\',"
					+ "time = CURRENT_TIMESTAMP, activity = \'\' WHERE 1=3");
		} catch (SQLException e) {
			String theError = e.getSQLState();
			// System.out.println("  Utils GOT:  " + theError);
			/** If table exists will get - WARNING 02000: No row was found **/
			if (theError.equals("42X05")) // Table does not exist
			{
				Server.infoStream.println("Table user_record"
						+ "does not exist.");
				Server.infoStream.println("Creating now...");
				try {
					Statement s = conn.createStatement();
					s.execute("CREATE TABLE user_record"
							+ "(username VARCHAR(32) NOT NULL PRIMARY KEY, "
							+ "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, "
							+ "activity VARCHAR(32) NOT NULL)");
					return;
				} catch (SQLException se) {
					Server.infoStream
							.println("Failed to create table user_record.");
					throw se;
				}
			} else if (theError.equals("42X14") || theError.equals("42821")) {
				Server.infoStream
						.println("Incorrect table definition. Drop table WISH_LIST and rerun this program.");
				throw e;
			} else {
				Server.infoStream.println("Unhandled SQLException.");
				throw e;
			}
		}
		Server.infoStream.println("Table user_record was checked.");
	}

	public void putRecord(Record record) throws SQLException {
		putRecord(record, conn.prepareStatement(
				"INSERT INTO user_record VALUES (?, ?, ?)"));
	}
	
	public void putRecord(Record record,
			PreparedStatement ps) throws SQLException {
		ps.setString(1, record.getUsername());
		ps.setTimestamp(2, new Timestamp(record.getTime().getTime()));
		ps.setString(3, record.getActivity());
		ps.execute();
	}
	
	public ArrayList<Record> searchRecord(String username) throws SQLException {
		return searchRecord(username, conn.prepareStatement(
				"SELECT time, activity "
				+ "FROM user_record "
				+ "WHERE username=?"));
	}

	public ArrayList<Record> searchRecord(String username, 
			PreparedStatement ps) throws SQLException {
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		ArrayList<Record> ret = new ArrayList<Record>();
		while(rs.next()){
			Date time = rs.getTimestamp(1);
			String activity = rs.getString(2);
			ret.add(new Record(username, time, activity));
		}
		return ret;
	}
	
	private Connection conn;
}
