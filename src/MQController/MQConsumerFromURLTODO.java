/**
 * @author vito
 * time	2016-5-8
 */
package MQController;

import java.beans.Transient;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.text.AbstractDocument.Content;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.sleepycat.je.Database;

import WebPage.DownloadThread;

public class MQConsumerFromURLTODO implements Runnable {

	private String targetDestination = null;

	public static int currentMessageCount;

	public MQConsumerFromURLTODO() {
		// TODO Auto-generated constructor stub
		targetDestination = "tcp://192.168.1.28:61616";
		currentMessageCount = 0;
	}

	public MQConsumerFromURLTODO(String target) {
		// TODO Auto-generated constructor stub
		targetDestination = target;
		currentMessageCount = 0;
	}

	@Override
	public synchronized void run() {
		// TODO Auto-generated method stub
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, targetDestination);
			Connection connection = factory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("urltodo");
			MessageConsumer consumer = session.createConsumer(destination);
			// create thread pool
			ExecutorService pool = Executors.newCachedThreadPool();

			while (true) {
				// if the thread count in pool and it's queue larger than 20
				// then stop add task
				if (currentMessageCount > 6) {
					Thread.sleep(5000);
					continue;
				}
				Message message = consumer.receive();
				if (message == null) {
					break;
				}
				currentMessageCount++;
				TextMessage text = (TextMessage) message;
				Callable<String> dlThread = new DownloadThread(text.getText());
				Future<String> result = pool.submit(dlThread);
				String content = null;
				while(true){
					if(result.isDone()){
						content = result.get();
						break;
					}
					Thread.sleep(3000);
				}
				//it must be a log part
				if(content == null){
					System.out.println("failed to get page ------ " + message);
				}
				//store the page
				
				
				//extract URLs
				
				
			}

			pool.shutdown();
			session.close();
			connection.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
