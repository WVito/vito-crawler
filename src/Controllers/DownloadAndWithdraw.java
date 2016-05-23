package Controllers;

import java.util.List;

import FileParser.FormatURLsInterface;
import FileParser.HtmlParserClass;
import FileParser.URLFilterInterface;
import FileParser.URLRetrieveInterface;
import MQController.MQParameter;
import PageDB.MongoDBParameter;
import PageDB.PageDB;
import WebPage.Downloader;
import WebPage.HtmlDownLoader;

public class DownloadAndWithdraw implements Runnable {
	/**
	 * the URL this thread will process
	 */
	private final String url;
	/**
	 * MongoDB related parameters
	 */
	private final MongoDBParameter mongoDBParameter;
	/**
	 * message queue related parameters
	 */
	private final MQParameter mqParameter;
	
	/**
	 * Constructor
	 * process page URL with information provided in MongoDBParameter and MQParameter
	 * 
	 * @param url target web page
	 * @param mongoDBParameter MongoDB parameters
	 * @param mqParameter message queue parameters
	 */
	public DownloadAndWithdraw(String url, MongoDBParameter mongoDBParameter, MQParameter mqParameter){
		this.url = url;
		this.mongoDBParameter = mongoDBParameter;
		this.mqParameter = mqParameter;
	}

	/**
	 * override run() of java.lang.Runnable.
	 * this thread will download page, store page, withdraw URLs and produce URLs to message queue.
	 * in the four steps, page storing and URLs producing would activate it's own thread. 
	 */
	@Override
	public void run() {
		
		// download page
		Downloader downloader = new HtmlDownLoader(url);
		downloader.requestContent();
		if(!downloader.isRequestSuccessful()){			//if request page not successful
			System.out.println("request \"" + url + " \" failed.");
			return;
		}
		String content = downloader.getContent();
		
		// activate thread to store page
		Runnable pageDB = new PageDB(url, content, mongoDBParameter);
		Thread storeInDB = new Thread(pageDB);
		storeInDB.start();
		
		//withdraw URLs
		URLRetrieveInterface urlRetriever = new HtmlParserClass(url, content);
		List<String> urls = urlRetriever.retriveURLFromHtml();
		if(urls == null) return;
		List<String> urlfromJS = urlRetriever.retriveURLFromJS();
		if(urlfromJS != null){
			urls.addAll(urlfromJS);
		}
		URLFilterInterface urlFilter = (URLFilterInterface)urlRetriever;
		urls = urlFilter.filterJsCssUrl(urls);
		if(urls == null) return;
		FormatURLsInterface urlFormater = (FormatURLsInterface)urlFilter;
		urls = urlFormater.formatURLS(urls);
		if(urls == null) return;
		
		//activate thread to produce URLs to MQ
		Runnable urlfilter = new URLFilterproducer(this.mqParameter, urls);
		Thread urlFilterThread = new Thread(urlfilter);
		urlFilterThread.start();
		
		notifyAll();
	}
}
