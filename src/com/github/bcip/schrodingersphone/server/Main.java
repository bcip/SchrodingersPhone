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
			server.dm.putRecord(new Record("wang", new Date(), "sitting"));
			Thread.sleep(1000);
			ArrayList<Record> records = server.dm.searchRecord("wang");
			System.out.println(records.get(0).getActivity());
		}
		finally{
			server.stop();
		}
	}

}
