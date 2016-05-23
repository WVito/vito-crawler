/**
 * this thread processes messages in the message pool,
 * 
 */
package Controllers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import MQController.MQParameter;
import PageDB.MongoDBParameter;

public class MessageProcess implements Runnable {

	/**
	 * DownloadAndWithdraw thread count
	 */
	private int subThreadCount;
	
	/**
	 * MongoDB related parameters
	 */
	private final MongoDBParameter mongoDBParameter;
	
	/**
	 * message queue related parameters
	 */
	private final MQParameter mqParameter;
	
	/**
	 * Constructor
	 * need MQParameter, MongoDBParameter and the count of sub HTML request thread
	 * 
	 * @param mqParameter include IP, port, queue name, user and password.
	 * @param mongoDBParameter	include IP, port, database name and collection name
	 * @param subThreadCount	the count of HTML request thread
	 */
	public MessageProcess( MQParameter mqParameter, MongoDBParameter mongoDBParameter, int subThreadCount) {
		this.mqParameter = mqParameter;
		this.mongoDBParameter = mongoDBParameter;
		this.subThreadCount = subThreadCount;
	}
	
	/**
	 * override run() of java.lang.Runnable.
	 * this thread will get message from message pool,
	 * activate DownloadAndWithdraw thread to 
	 * download page, store page, withdraw URLs and produce to message queue.
	 */
	@Override
	public void run() {
		ExecutorService pool = Executors.newFixedThreadPool(subThreadCount);
		String url = null;
		try {
			while (true) {
				//check active sub-thread count
				if (((ThreadPoolExecutor) pool).getActiveCount() >= subThreadCount) {
					wait();	//<!----test the locker---->
					continue;
				}
				//check the message pool count
				if( MessagePool.getLength() <= 0 ){
					wait();
					continue;
				}
				//add sub-thread to pool
				url = MessagePool.pop();
				Runnable downloader = new DownloadAndWithdraw(url, this.mongoDBParameter, this.mqParameter);
				pool.execute(downloader);
				
				notifyAll();
			}
		} catch (Exception e) {
			System.out.println("activate sub-thread to process "+url+"failed." );
			//e.printStackTrace();
		} finally {
			if(((ThreadPoolExecutor)pool).getActiveCount() > 0 ){
				pool.shutdownNow();
			}
		}
	}

}
