package com.github.bcip.schrodingersphone.listener;

import com.github.bcip.schrodingersphone.Record;

public class Main {
	public static void main(String[] args) throws Exception{
		Downloader dl = new Downloader(ServerInfo.serverAddress, ServerInfo.port);
		while(true){
			Record record = dl.getRecentRecord("Lijie");
			System.out.println(record.getActivity());
			System.out.println(record.getTime());
			Thread.sleep(10000);
		}
	}
}
