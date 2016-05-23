package MQController;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

public class MQProducer extends MQBaseClass {

	/**
	 * MessageProducer,
	 * the needed session and connection constructed in super class
	 */
	private MessageProducer producer;
	
	/**
	 * Constructor,
	 * need IP, port, and queue name of target message queue,
	 * the factory, connection and session constructed in super class
	 * 
	 * @param ip
	 * @param port
	 * @param queueName
	 */
	public MQProducer(String ip, String port, String queueName){
		super(ip, port, queueName);
		try {
			producer = this.session.createProducer(this.destination);
		} catch (JMSException e) {
			System.out.println("create producer "+mqParameter.ip +":"+mqParameter.port+"\t"+mqParameter.queueName+ " failed.");
			//e.printStackTrace();
		}
	}
	
	/**
	 * Constructor,
	 * need IP, port, queue name, user and password of target message queue,
	 * the factory, connection, and session constructed in super class
	 * 
	 * @param ip
	 * @param port
	 * @param queueName
	 * @param user the user name of queue, by default, it's ActiveMQConnection.DEFAULT_USER
	 * @param pwd this password of queue, by default, it's ActiveMQConnection.DEFAULT_PASSWORD
	 */
	public MQProducer(String ip, String port, String queueName, String user, String pwd) {
		super(ip, port, queueName, user, pwd);
		try {
			producer = this.session.createProducer(this.destination);
		} catch (JMSException e) {
			System.out.println("create producer "+mqParameter.ip+":"+mqParameter.port+"\t"+mqParameter.queueName+" failed.");
			//e.printStackTrace();
		}
	}
	/**
	 * Constructor,
	 * need MQParameter type object,
	 * the factory, connection, and session constructed in super class
	 * 
	 * @param param
	 */
	public MQProducer(MQParameter param){
		super(param);
		try {
			producer = this.session.createProducer(this.destination);
		} catch (Exception e) {
			System.out.println("create producer "+mqParameter.ip+":"+mqParameter.port+"\t"+mqParameter.queueName+" failed.");
			//e.printStackTrace();
		}
	}
	
	/**
	 * use producer to send message to target message queue
	 * 
	 * @param message
	 */
	public void sendMessage(String message){
		try {
			TextMessage textMessage = this.session.createTextMessage();
			textMessage.setText(message);
			this.producer.send(textMessage);
		} catch (JMSException e) {
			System.out.println("message: "+message+" transfer to "+mqParameter.ip+":"+mqParameter.port+"\t"+mqParameter.queueName+" failed.");
			//e.printStackTrace();
		}
	}
}