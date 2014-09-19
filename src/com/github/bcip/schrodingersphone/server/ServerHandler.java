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
		//TODO
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
		//TODO
		System.out.println("Package handled");
	}
	
	public SPMessage handlePut(SPMessage request) throws SPException{
		Record record = request.getRecord();
		record.setActivity(server.pd.predict(record.getFeature()).toString());
		try{
			dm.putRecord(record, pss);
		} catch (SQLException e) {
			throw new SPException(e.getMessage());
		}
		
		return SPMessage.newPutResponse();
	}
	
	public SPMessage handleSearch(SPMessage request)throws SPException{
		String username = request.getUsername();
		Record[] records;
		try{
			records = (Record[])dm.searchRecord(username, pss).toArray();
		} catch (SQLException e) {
			throw new SPException(e.getMessage());
		}
		return SPMessage.newSearchResponse(records);
	}
	
	public SPMessage handleGetRecent(SPMessage request)throws SPException{
		String username = request.getUsername();
		Record record;
		try{
			record = dm.getRecentRecord(username, pss);
		} catch (SQLException e) {
			throw new SPException(e.getMessage());
		}
		return SPMessage.newGetRecentResponse(record);
	}
	
	private Server server;
	private DataManager dm;
	PreparedStatementSet pss;
	
}
