package com.github.bcip.schrodingersphone.server;

import java.net.InetAddress;
import java.util.Date;

import com.github.bcip.schrodingersphone.Feature;
import com.github.bcip.schrodingersphone.Predictor;
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
		Predictor pd = new Predictor();
		pd.load("coef");
		Server server = new Server(driver, databaseURL, pd);
		server.start();
		try{
			//Uploader uploader = new Uploader(InetAddress.getByName("59.66.130.141"), 5641);
			//uploader.uploadRecord(new Record("wang", new Date(), new Feature(new double[3][43])));
			while(System.in.read() != (int)'q')
				;
		}
		finally{
			server.stop();
		}
	}

}
