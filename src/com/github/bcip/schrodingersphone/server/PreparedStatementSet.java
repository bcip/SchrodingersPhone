package com.github.bcip.schrodingersphone.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementSet {
	public PreparedStatementSet(Connection conn) throws SQLException{
		putRecord = conn.prepareStatement(putStatement);
		searchRecord = conn.prepareStatement(searchStatement);
		getRecentRecord = conn.prepareStatement(getRecentStatement);
	}
	
	PreparedStatement putRecord;
	PreparedStatement searchRecord;
	PreparedStatement getRecentRecord;
	
	public static String putStatement = "INSERT INTO user_record VALUES (?, ?, ?)";
	public static String searchStatement = "SELECT time, activity FROM user_record WHERE username=?";
	public static String getRecentStatement = "SELECT time, activity FROM user_record WHERE username=?"
			+ " ORDER BY time DESC FETCH FIRST ROW ONLY";
}
