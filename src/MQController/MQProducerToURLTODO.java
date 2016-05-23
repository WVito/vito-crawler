/**
 * @author vito
 * time:	2016-5-8
 */
package MQController;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MQProducerToURLTODO implements Runnable {

	private String tartDestination = null;
	private String MessageContent = null;

	public MQProducerToURLTODO( String message) {
		// TODO Auto-generated constructor stub
		this.MessageContent = message;
		this.tartDestination = "tcp://192.168.1.28:61616";
	}
	
	public MQProducerToURLTODO( String dest, String message ) {
		// TODO Auto-generated constructor stub
		this.tartDestination = dest;
		this.MessageContent = message;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, tartDestination);
			Connection connection = factory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("urltodo");
			MessageProducer producer = session.createProducer(destination);
			TextMessage message = session.createTextMessage();
			message.setText(MessageContent);
			producer.send(message);
			//log part should be added
			
			//
			session.close();
			connection.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
