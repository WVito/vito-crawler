package Entrance;

import FileParser.XmlParser;

public class Entrance {
	
	//private static XmlParser xmlParser = null;
	public static void main(String[] args) {
		
		XmlParser xmlParser = new XmlParser();
		if(xmlParser.xmlData.urlTODO.isActivated){
			runMQ_URLTODO();
			System.out.println("MQ_URL_TODO running");
		}
		if(xmlParser.xmlData.urlFilter.isActivated){
			runMQ_URLFilter();
			System.out.println("MQ_URL_FILTER running");
		}
		if(xmlParser.xmlData.pageDB.isActivated){
			runDB_Page();
			System.out.println("DB_PAGE running");
		}
		if(xmlParser.xmlData.filterDB.isActivated){
			runDB_Filter();
			System.out.println("DB_FILTER running");
		}
		if(xmlParser.xmlData.downloader.isActivated){
			runDownLoader();
			System.out.println("DOWNLOADER running");
		}

		System.out.println("program out!");
	}
	
	public static void runMQ_URLTODO() {
		//
	}
	
	public static void runMQ_URLFilter(){
		//produer: produce message to MQ urltodo
		//consumer: consume message from MQ urlfilter
		//store: store URLs in DB
	}
	
	public static void runDB_Page(){
		
	}
	
	public static void runDB_Filter(){
		
	}
	
	public static void runDownLoader(){
		//Get HTML 
	}
}
