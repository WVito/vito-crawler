package Controllers;

import java.util.List;

import MQController.MQParameter;
import MQController.MQProducer;

public class URLFilterproducer extends MQProducer implements Runnable {

	/**
	 * URLs to be filtered
	 */
	private List<String> urls;
	
	/**
	 * Constructor
	 * parameters and database connection stored in super classes
	 * 
	 * @param param
	 */
	public URLFilterproducer(MQParameter param, List<String> urls) {
		super(param);
		this.urls = urls;
	}

	/**
	 * override run() o java.lang.Runnable
	 * this thread will produce URLs to message queue URLFilter
	 */
	@Override
	public void run() {
		//produce to URLFilter
		try {
			for(int i = 0; i < urls.size(); i++){
				this.sendMessage(urls.get(i));
			}
		} catch (Exception e) {
			System.out.println("produce message to "+this.mqParameter.ip+":"+this.mqParameter.port+" "+this.mqParameter.queueName+" failed.");
			//e.printStackTrace();
		}finally{
			this.close();
		}
	}
	
	
}
