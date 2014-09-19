package com.github.bcip.schrodingersphone;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class SPMessage implements Serializable{
	
	private static final long serialVersionUID = -4832052694451956828L;

	public SPMessage(SPException spe){
		this.type = STR_EXCEPTION;
		this.spe = spe;
	}
	
	public String getType(){
		return type;
	}
	
	public Record getRecord(){
		return record;
	}
	
	public String getUsername(){
		return username;
	}
	
	public Record[] getRecords(){
		return records;
	}
	
	public SPException getSPException(){
		return spe;
	}

	public void sendMessage(Socket sock) {
		try{
			ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
			out.writeObject(this);
			out.flush();
			sock.shutdownOutput();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static SPMessage newPutRequest(Record record){
		SPMessage msg = new SPMessage(STR_PUT);
		
		msg.record = record;
		return msg;
	}
	
	public static SPMessage newPutResponse(){
		SPMessage msg = new SPMessage(STR_RESP);
		return msg;
	}
	
	public static SPMessage newSearchRequest(String username){
		SPMessage msg = new SPMessage(STR_SEARCH);
		msg.username = username;
		return msg;
	}
	
	public static SPMessage newSearchResponce(Record[] records){
		SPMessage msg = new SPMessage(STR_RESP);
		msg.records = records;
		return msg;
	}
	
	public static SPMessage newGetRecentRequest(String username){
		SPMessage msg = new SPMessage(STR_GET_RECENT);
		msg.username = username;
		return msg;
	}
	
	public static SPMessage newGetRecentResponse(Record record){
		SPMessage msg = new SPMessage(STR_GET_RECENT);
		msg.record = record;
		return msg;
	}
	
	public static SPMessage loadFromSocket(Socket sock) throws SPException{
		try{
			sock.setSoTimeout(TIMEOUT);
			ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			SPMessage msg = (SPMessage)in.readObject();
			if(msg.type.equals(STR_EXCEPTION)){
				throw msg.spe;
			}
			return msg;
		} catch (IOException e){
			e.printStackTrace();
			throw new SPException("Failed to read message");
		} catch (ClassNotFoundException e){
			throw new SPException("Failed to read message");
		} catch (NullPointerException e) {
			throw new SPException("Invalid format");
		}
		
	}

	private SPMessage(String type){
		this.type = type;
	}
	
	private String type;
	private String username;
	private SPException spe;
	private Record record;
	private Record[] records;
	
	public static final String STR_EXCEPTION = "SPException";
	public static final String STR_PUT = "put";
	public static final String STR_SEARCH = "search";
	public static final String STR_GET_RECENT = "getRecent";
	public static final String STR_RESP = "resp";
	public static final int TIMEOUT = 2000;
	
}
