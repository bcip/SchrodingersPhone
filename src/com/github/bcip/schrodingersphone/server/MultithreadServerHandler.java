package com.github.bcip.schrodingersphone.server;

import java.net.Socket;
import java.sql.SQLException;

import com.github.bcip.schrodingersphone.network.NetworkHandler;

public class MultithreadServerHandler implements NetworkHandler {
	
	public MultithreadServerHandler (Server server, int numThreads) throws SQLException{
		this.server = server;
		this.threadPool = new ThreadPool(numThreads, server.conn);
	}
	
	public void stop(){
		threadPool.stop();
	}

	@Override
	public void handle(Socket sock) {
		try{
			threadPool.addJob(new HandlerJob(sock));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private class HandlerJob implements Runnable {
		public HandlerJob(Socket sock){
			this.sock = sock;
		}
		
		@Override
		public void run(){
			System.out.println("In thread "+Thread.currentThread().getId());
			new ServerHandler(server).handle(sock);
		}
		
		private Socket sock;
	}
	
	private Server server;
	private ThreadPool threadPool;

}
