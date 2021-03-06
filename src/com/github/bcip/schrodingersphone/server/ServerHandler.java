package com.github.bcip.schrodingersphone.server;

import java.net.Socket;
import java.sql.SQLException;

import com.github.bcip.schrodingersphone.Record;
import com.github.bcip.schrodingersphone.SPException;
import com.github.bcip.schrodingersphone.SPMessage;
import com.github.bcip.schrodingersphone.network.NetworkHandler;

public class ServerHandler implements NetworkHandler {

	public ServerHandler(Server server){
		this.server = server;
		this.dm = server.dm;
	}
	
	@Override
	public void handle(Socket sock) {
		System.out.println("Package got.");
		Thread thread = Thread.currentThread();
		if(!(thread instanceof PreparedThread)){
			try{
				pss = dm.prepareStatements();
				System.err.println("Warning: ServerHandler is running on a non-prepared thread.");
			} catch (SQLException e){
				new SPMessage(new SPException("Server internal exception")).sendMessage(sock);
				e.printStackTrace();
				return;
			}
		}
		else{
			pss = ((PreparedThread)thread).getPreparedStatementSet();
		}

		SPMessage response = null;
		try{
			SPMessage request = SPMessage.loadFromSocket(sock);
			System.out.println(request.getType());
			if(request.getType().equals(SPMessage.STR_PUT)){
				response = handlePut(request);
			}
			else if(request.getType().equals(SPMessage.STR_SEARCH)){
				response = handleSearch(request);
			}
			else if(request.getType().equals(SPMessage.STR_GET_RECENT)){
				response = handleGetRecent(request);
			}
			else{
				throw new SPException("Invalid request format");
			}
		} catch (SPException e){
			e.printStackTrace();
			response = new SPMessage(e);
		} catch (NullPointerException e){
			response = new SPMessage(new SPException("Invalid request format"));
		}
		response.sendMessage(sock);
		System.out.println("Package handled");
	}
	
	public SPMessage handlePut(SPMessage request) throws SPException{
		Record record = request.getRecord();
		record.setActivity(server.pd.predict(record.getFeature()).toString());
		System.out.println(record.getUsername() + " is " + record.getActivity() + " now.");
		try{
			dm.putRecord(record, pss);
		} catch (SQLException e) {
			throw new SPException(e.getMessage());
		}
		
		return SPMessage.newPutResponse();
	}
	
	public SPMessage handleSearch(SPMessage request)throws SPException{
		//TODO
		return null;
	}
	
	public SPMessage handleGetRecent(SPMessage request)throws SPException{
		//TODO
		return null;
	}
	
	private Server server;
	private DataManager dm;
	PreparedStatementSet pss;
	
}
