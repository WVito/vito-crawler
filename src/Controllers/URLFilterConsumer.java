package Controllers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import MQController.MQConsumer;
import MQController.MQParameter;

public class URLFilterConsumer extends MQConsumer implements Runnable {

	private int MQ_THREAD_COUNT = 10;

	public URLFilterConsumer(MQParameter param) {
		super(param);
		// TODO Auto-generated constructor stub
	}

	public URLFilterConsumer(String ip, String port, String queueName) {
		super(ip, port, queueName);
		// TODO Auto-generated constructor stub
	}

	public URLFilterConsumer(String ip, String port, String queueName, String user, String pwd) {
		super(ip, port, queueName, user, pwd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		ExecutorService pool = Executors.newFixedThreadPool(this.MQ_THREAD_COUNT);
		try {
			while (true) {
				// check thread pool status
				if (((ThreadPoolExecutor) pool).getActiveCount() >= this.MQ_THREAD_COUNT) {
					wait();
					continue;
				}
				// receive URL
				String message = this.receiveMessage();
				if (message == null) {
					continue;
				}
				// filter and produce current URL
				Runnable filterProducer = new FilterAndProduceThread(message, this.mqParameter);
				pool.execute(filterProducer);
			}
		} catch (Exception e) {
			System.out.println("recieve url from " + this.mqParameter.ip + ":" + this.mqParameter.port + " "
					+ this.mqParameter.queueName + " failed.");
		} finally {
			try {
				this.close();
				pool.shutdownNow();
			} catch (Exception e) { }
		}
	}
}
