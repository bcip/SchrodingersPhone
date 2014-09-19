package com.github.bcip.schrodingersphone.listener;

public class Main {
	public static void main(String[] args) throws Exception{
		Downloader dl = new Downloader(ServerInfo.serverAddress, ServerInfo.port);
		System.out.println(dl.getRecentRecord("wang").getActivity());
	}
}
