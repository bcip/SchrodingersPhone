package com.github.bcip.schrodingersphone.sensor;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import android.util.Log;

import com.github.bcip.schrodingersphone.Record;
import com.github.bcip.schrodingersphone.SPException;
import com.github.bcip.schrodingersphone.SPMessage;

/**
 * A class which upload the feature we get in client to the remote server
 * 
 * @author wjmzbmr
 * 
 */
public class Uploader {

	MainActivity activity;

	public Uploader(InetAddress host, int port, MainActivity activity) {
		this.host = host;
		this.port = port;
		this.activity = activity;
	}

	public void uploadRecord(Record record) throws SPException {
		new myThread(record).start();
	}

	Socket connectHost() throws SPException {
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

	void closeHost(Socket sock) throws SPException {
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

	private static final int TIMEOUT_MILLISECONDS = 1000;

	/**
	 * We can not access network in the main thread, so we should write a class
	 * to do that.
	 * 
	 * @author wjmzbmr
	 * 
	 */
	class myThread extends Thread {
		Record record;

		public myThread(Record record) {
			// TODO Auto-generated constructor stub
			this.record = record;
		}

		@Override
		public void run() {
			try {
				SPMessage request = SPMessage.newPutRequest(record);
				Socket sock = connectHost();
				try {
					request.sendMessage(sock);
					SPMessage.loadFromSocket(sock);
				} finally {
					closeHost(sock);
				}
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				// e.printStackTrace();
				System.out.println(e);
			}
		}
	}
}
