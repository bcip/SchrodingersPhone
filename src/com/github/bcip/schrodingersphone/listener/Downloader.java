package com.github.bcip.schrodingersphone.listener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.github.bcip.schrodingersphone.Record;
import com.github.bcip.schrodingersphone.SPException;
import com.github.bcip.schrodingersphone.SPMessage;

public class Downloader {

	public Downloader(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public Record[] searchRecord(String username) throws SPException{
		SPMessage request = SPMessage.newSearchRequest(username);
		Socket sock = connectHost();
		try{
			request.sendMessage(sock);
			SPMessage response = SPMessage.loadFromSocket(sock);
			return response.getRecords();
		} finally {
			closeHost(sock);
		}
	}
	
	public Record getRecentRecord(String username) throws SPException{
		SPMessage request = SPMessage.newGetRecentRequest(username);
		Socket sock = connectHost();
		try{
			request.sendMessage(sock);
			SPMessage response = SPMessage.loadFromSocket(sock);
			return response.getRecord();
		} finally {
			closeHost(sock);
		}
	}
	
	private Socket connectHost() throws SPException {

		try {
			Socket socket = new Socket(this.host, this.port);
			socket.setSoTimeout(TIMEOUT_MILLISECONDS);
			return socket;
		} catch (UnknownHostException e1) {
			throw new SPException("Could not create socket");
		} catch (IOException e2) {
			throw new SPException("Could not connect to server");
		}
	}

	private void closeHost(Socket sock) throws SPException {
		if (sock == null) {
			return;
		}
		try {
			sock.close();
		} catch (IOException e) {
			throw new SPException("Error occured when closing socket");
		}
	}
	
	private InetAddress host;
	private int port;

	private static final int TIMEOUT_MILLISECONDS = 2000;
	
}
