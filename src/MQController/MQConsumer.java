package MQController;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

public class MQConsumer extends MQBaseClass {

	/**
	 * MessageConsumer,
	 * the needed session and connection constructed in super class
	 */
	private MessageConsumer consumer;
	
	/**
	 * Constructor
	 * need IP, port, and queue name of target message queue,
	 * the factory, connection and session constructed in super class
	 * 
	 * @param ip
	 * @param port
	 * @param queueName
	 */
	public MQConsumer(String ip, String port, String queueName){
		super(ip, port, queueName);
		try{
			consumer = this.session.createConsumer(this.destination);
		}catch(JMSException e){
			System.out.println("create consumer "+mqParameter.ip+":"+mqParameter.port+"\t"+mqParameter.queueName+" failed.");
			//e.printStackTrace();
		}
	}
	
	/**
	 * Constructor
	 * need IP, port, queue name, user and password of target message queue,
	 * the factory, connection, and session constructed in super class
	 * 
	 * @param ip
	 * @param port
	 * @param queueName
	 * @param user the user name of queue, by default, it's ActiveMQConnection.DEFAULT_USER
	 * @param pwd this password of queue, by default, it's ActiveMQConnection.DEFAULT_PASSWORD
	 */
	public MQConsumer(String ip, String port, String queueName, String user, String pwd) {
		super(ip, port, queueName, user, pwd);
		try{
			consumer = this.session.createConsumer(this.destination);
		}catch(JMSException e){
			System.out.println("create consumer "+mqParameter.ip+":"+mqParameter.port+"\t"+mqParameter.queueName+" failed.");
			//e.printStackTrace();
		}
	}
	
	/**
	 * Constructor,
	 * need MQParameter
	 * the factory, connection, and session constructed in super class
	 * 
	 * @param param MQParameter type object
	 */
	public MQConsumer(MQParameter parameter){
		super(parameter);
		try {
			consumer = this.session.createConsumer(this.destination);
		} catch (Exception e) {
			System.out.println("create consumer "+mqParameter.ip+":"+mqParameter.port+"\t"+mqParameter.queueName+" failed.");
			//e.printStackTrace();
		}
	}
	
	/**
	 * use consumer to receive message from target message queue
	 * 
	 * @return message
	 */
	public String receiveMessage(){
		String msg = null;
		try{
			Message message = consumer.receive();
			if(message == null) return null;
			TextMessage textMessage = (TextMessage)message;
			msg = textMessage.getText();
		}catch(JMSException e){
			System.out.println("receive message from "+mqParameter.ip+":"+mqParameter.port+"\t"+mqParameter.queueName+" failed.");
			//e.printStackTrace();
		}
		return msg;
	}
}