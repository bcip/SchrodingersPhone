package com.github.bcip.schrodingersphone.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * This is a generic class that should handle all TCP network connections
 * arriving on a unique port. Ensure that this class remains generic by
 * providing the connection handling logic in a NetworkHandler.
 */
public class SocketServer {

	private int port;
	private ServerSocket server;
	private NetworkHandler handler;
	private boolean stopped = false;

	private static final int TIMEOUT = 100;

	/**
	 * Construct a SocketServer with a ServerSocket listening on a free port.
	 */
	public SocketServer() {
		this(0);
	}

	/**
	 * Construct a SocketServer with a ServerSocket listening on the port passed
	 * in.
	 * 
	 * @param port
	 *            port on which to listen for connections
	 */
	public SocketServer(int port) {
		this.port = port;
	}

	public int getPort() {
		return this.port;
	}

	/**
	 * Add the network handler for this socket server
	 * 
	 * @param handler
	 *            is logic for servicing a network connection
	 */
	public void addHandler(NetworkHandler handler) {
		this.handler = handler;
	}

	/**
	 * Creates a new ServerSocket and binds it to a port. If the given port
	 * is 0, the ServerSocket should be bound to an automatically allocated
	 * port. See ServerSocket documentation for details.
	 * 
	 * @throws IOException
	 *             if unable create and bind a ServerSocket
	 */
	public void connect() throws IOException {
		this.server = new ServerSocket(port);
	}

	/**
	 * Accept and save requests as jobs to be serviced asynchronously. A call to
	 * stop() should result in the closing of the ServerSocket within TIMEOUT
	 * milliseconds.
	 * 
	 * @throws IOException
	 *             if there is an unexpected network error while listening for
	 *             or servicing requests
	 */
	public void start() throws IOException {
		server.setSoTimeout(TIMEOUT);
		new Thread() {
			public void run() {
				while (!stopped) {
					try {
						Socket socket = server.accept();
						handler.handle(socket);
					} catch(SocketTimeoutException e){
						//ignore
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * Stops the ServerSocket cleanly (does not force an exception to be
	 * thrown). A call to stop() will result in the closing of the server no
	 * more than TIMEOUT milliseconds later. That logic should be implemented in
	 * start().
	 */
	public void stop() {
		stopped = true;
	}

	public NetworkHandler getHandler() {
		return handler;
	}

}
