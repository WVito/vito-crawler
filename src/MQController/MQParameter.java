package MQController;

import org.apache.activemq.ActiveMQConnection;

public class MQParameter {
	/**
	 * ip of queue running on
	 */
	public String ip;
	
	/**
	 * port to communicate
	 */
	public String port;
	
	/**
	 * queue name to communicate
	 */
	public String queueName;
	
	/**
	 * user name of queue
	 */
	public String user;
	
	/**
	 * password of queue
	 */
	public String password;
	
	/**
	 * Constructor
	 * need ip, port, queueName, user and password of target
	 * 
	 * @param ip
	 * @param port
	 * @param queueName
	 * @param user
	 * @param pwd
	 */
	public MQParameter( String ip, String port, String queueName, String user, String pwd){
		this.ip = ip;
		this.port = port;
		this.queueName = queueName;
		this.user = user;
		this.password = pwd;
	}
	
	/**
	 * Constructor
	 * need IP, port and queueName of message queue,
	 * user and password are provided by ActiveMQ by default.
	 * 
	 * @param ip
	 * @param port
	 * @param queueName
	 */
	public MQParameter( String ip, String port, String queueName){
		this.ip = ip;
		this.port = port;
		this.queueName = queueName;
		this.user = ActiveMQConnection.DEFAULT_USER;
		this.password = ActiveMQConnection.DEFAULT_PASSWORD;
	}
}
