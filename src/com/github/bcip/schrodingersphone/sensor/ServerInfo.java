package com.github.bcip.schrodingersphone.sensor;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * A class which stores the server's information
 * 
 * @author wjmzbmr
 * 
 */

public class ServerInfo {
	static public InetAddress serverAddress;
	static public int port;

	static {
		try {
			serverAddress = InetAddress.getByName("59.66.130.141");
			port = 5641;
		} catch (UnknownHostException e) {
			serverAddress = null;
			port = 0;
		}
	}
}
