package Controllers;

import BloomFilter.BloomFilter;
import MQController.MQParameter;
import MQController.MQProducer;

public class FilterAndProduceThread extends MQProducer implements Runnable {

	private String url;

	/**
	 * Constructor need MQParameter type object initialize factory, connection,
	 * session, destination and consumer in super classes with these provided
	 * arguments
	 * 
	 * @param url
	 *            the URL to be filtered
	 * @param parameter
	 *            parameter to initialize message queue
	 */
	public FilterAndProduceThread(String url, MQParameter parameter) {
		super(parameter);
		this.url = url;
	}

	/**
	 * Constructor, need IP, port and queue name of target message queue
	 * initialize factory, connection, session, destination and consumer in
	 * super classes with these provided arguments
	 * 
	 * @param url
	 *            the URL to be filtered
	 * @param ip
	 *            the IP of message queue
	 * @param port
	 *            the port to connect message queue
	 * @param queueName
	 *            queue name of URLTODO
	 */
	public FilterAndProduceThread(String url, String ip, String port, String queueName) {
		super(ip, port, queueName);
		this.url = url;
	}

	/**
	 * Constructor, need IP, port, queue name, user and password initialize
	 * factory, connection, session, destination and consumer in super classes
	 * with these provided arguments
	 * 
	 * @param url
	 *            the URL to be filtered
	 * @param ip
	 *            the IP of message queue
	 * @param port
	 *            the port to connect message queue
	 * @param queueName
	 *            queue name of URLTODO
	 * @param user
	 *            the user name of message queue
	 * @param pwd
	 *            the password of message queue
	 */
	public FilterAndProduceThread(String url, String ip, String port, String queueName, String user, String pwd) {
		super(ip, port, queueName, user, pwd);
		this.url = url;
	}

	@Override
	public void run() {
		//Filter this.url
		int[] fingerPrints = BloomFilter.calFingerPrint(url);
		if( BloomFilter.check( fingerPrints ) ) {				//if in BitSet, stop this thread
			this.close( );				//close MQ connection
			notifyAll();
			return;
		}
		BloomFilter.add(fingerPrints);
		notifyAll();	//released lock of BloomFilter, notify all other threads
		//produce to UTLTODO
		this.sendMessage(this.url);
	}

}
