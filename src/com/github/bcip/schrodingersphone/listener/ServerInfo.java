package com.github.bcip.schrodingersphone.listener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

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
