/**
 * @author vito
 * time:	2016-5-8
 */
package WebPage;

import java.util.concurrent.Callable;

import MQController.MQConsumerFromURLTODO;
import MQController.MQProducerToURLTODO;

public class DownloadThread implements Callable<String> {

	private String url = null;
	
	public DownloadThread( String url){
		this.url = url;
	}
	
	// @Override
	// public void run() {
	// // TODO download HTML of URL
	//
	//
	//
	// //indicate the count of instance of current DownloadThread
	// MQConsumerFromURLTODO.currentMessageCount--;
	// }

	@Override
	public synchronized String call() throws Exception {
		String content = null;
		try {
			//download HTML of URL
			HtmlDownLoader hdl = new HtmlDownLoader(url);
			content = hdl.RequestPage();
			if(content != null) return content;
		} finally {
			// TODO: handle finally clause
			MQConsumerFromURLTODO.currentMessageCount--;
		}
		return null;
	}
}
