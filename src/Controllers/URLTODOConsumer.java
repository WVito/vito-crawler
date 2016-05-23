package Controllers;

import MQController.MQConsumer;
import MQController.MQParameter;

public class URLTODOConsumer extends MQConsumer implements Runnable {
	
	/**
	 * Constructor
	 * need MQParameter type object
	 * initialize factory, connection, session, destination and consumer
	 * in super classes with these provided arguments
	 * 
	 * @param parameter
	 */
	public URLTODOConsumer(MQParameter parameter){
		super(parameter);
	}
	
	/**
	 * Constructor,
	 * need IP, port and queue name of target message queue
	 * initialize factory, connection, session, destination and consumer
	 * in super classes with these provided arguments
	 * 
	 * @param ip
	 * @param port
	 * @param queueName
	 */
	public URLTODOConsumer(String ip, String port, String queueName){
		super(ip, port, queueName);
	}
	
	/**
	 * Constructor,
	 * need IP, port, queue name, user and password
	 * initialize factory, connection, session, destination and consumer
	 * in super classes with these provided arguments
	 * 
	 * @param ip
	 * @param port
	 * @param queueName
	 * @param user
	 * @param pwd
	 */
	public URLTODOConsumer(String ip, String port, String queueName, String user, String pwd) {
		super(ip, port, queueName, user, pwd);
	}

	/**
	 * override run() of java.lang.Runnable,
	 * this thread will get message from URLTODO,
	 * and push it into MessagePool.
	 * 
	 */
	@Override
	public void run() {
		try {
			while(true){
				while( !MessagePool.canAdd( ) ){				//check whether message pool need to push more message.
					wait();
				}
				String message = this.receiveMessage( );
				if(message == null) {
					wait();
					continue;
				}
				while(!MessagePool.push(message)){		//keep trying to push message into message pool.
					wait();
				}
				notifyAll();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			this.close();
		}
	}
}