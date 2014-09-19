package com.github.bcip.schrodingersphone.server;

import java.util.ArrayList;
import java.util.Date;

import com.github.bcip.schrodingersphone.Record;

/**
 * The entry of server.
 * @author s-quark
 *
 */

public class Main {

	public static void main(String[] args) throws Exception {
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		String databaseURL = "jdbc:derby:SchrodingersPhoneDB";
		Server server = new Server(driver, databaseURL);
		server.start();
		try{
			System.in.read();
		}
		finally{
			server.stop();
		}
	}

}
