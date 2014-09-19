package com.github.bcip.schrodingersphone.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;

import com.github.bcip.schrodingersphone.Record;
import com.github.bcip.schrodingersphone.sensor.Uploader;

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
			Uploader uploader = new Uploader(InetAddress.getByName("59.66.130.141"), 5641);
			uploader.uploadRecord(new Record("wang", new Date(), "sitting"));
			System.in.read();
		}
		finally{
			server.stop();
		}
	}

}
