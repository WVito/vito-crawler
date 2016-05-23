package MQController;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MQBaseClass {
	
	/**
	 * MQ parameter object
	 */
	protected MQParameter mqParameter;
	/**
	 * ConnectionFactory
	 */
	protected ConnectionFactory factory;
	/**
	 * Connection
	 */
	protected Connection connection;
	/**
	 * Session
	 */
	protected Session session;
	/**
	 * Destination
	 */
	protected Destination destination;
	
	/**
	 * Constructor,
	 * need IP, port, queue name, user name and password of target message queue
	 * initialize factory, connection, session and destination with these information
	 * 
	 * @param ip
	 * @param port
	 * @param queueName
	 * @param user
	 * @param pwd
	 */
	public MQBaseClass(String ip, String port,String queueName, String user, String pwd){
		this.mqParameter = new MQParameter(ip, port, queueName, user, pwd);
		initializeActiveMQMember();
	}
	
	/**
	 * Constructor,
	 * need IP, port and queue name of target message queue,
	 * use default user and password provided by activeMQ.
	 * initialize factory, connection, session and destination with these information
	 * 
	 * @param ip
	 * @param port
	 * @param queueName
	 */
	public MQBaseClass(String ip, String port, String queueName){
		this.mqParameter = new MQParameter(ip, port, queueName);
		initializeActiveMQMember();
	}
	
	/**
	 * Constructor,
	 * use MQParameter object to construct MQ
	 * 
	 * @param param
	 */
	public MQBaseClass(MQParameter param){
		this.mqParameter = param;
		initializeActiveMQMember();
	}
	
	/**
	 * initialize factory, connection, session and destination
	 * with the IP, port, queue name, user and password provided
	 * by caller function
	 */
	private void initializeActiveMQMember(){
		try {
			factory = new ActiveMQConnectionFactory(mqParameter.user, mqParameter.password, "tcp://"+mqParameter.ip+":"+mqParameter.port);	//designed for tcp://192.168.1.3:61616
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(mqParameter.queueName);
		} catch (Exception e) {
			System.err.println("initialize queue " + this.mqParameter.queueName + " failed.");
		}
		
		return;
	}
	
	/**
	 * close this connection
	 */
	public void close(){
		try{
			session.close();
			connection.close();
			System.gc();
		}catch(Exception e){
			System.out.println("close message queue "+mqParameter.ip+":"+mqParameter.port+"\t"+mqParameter.queueName+" failed.");
		}
	}
	
	/**
	 * check whether there is a null field,
	 * if more than one of them is null,
	 * the coming process will fail.
	 * 
	 * @return
	 */
	public boolean isReady(){
		if(this.mqParameter== null ||
				this.factory == null ||
				this.connection == null ||
				this.session == null ||
				this.destination == null){
			return false;
		}
		return true;
	}
	
	/**
	 * write log to local log file,
	 * not completed yet.
	 */
	public void writeLog(){
		return;
	}
}
