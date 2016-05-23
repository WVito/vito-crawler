package Entrance;

import org.apache.activemq.ActiveMQConnection;

import BloomFilter.BloomFilter;
import Controllers.MessagePool;
import Controllers.MessageProcess;
import Controllers.URLFilterConsumer;
import Controllers.URLTODOConsumer;
import FileParser.XmlParser;
import MQController.MQProducer;

public class Entrance {
	/**
	 * need to be activated in main:
	 * URLTODOConsumer:		on slave
	 * MessagePool:					on slave
	 * MessageProcess:			on slave
	 * URLFilterConsumer:		on master
	 * BloomFilter						on master
	 * 
	 * @param args
	 * @throws Exception
	 */	
	public static void main(String[] args) throws Exception {
		System.out.println("program started!");
		
		System.out.println("parse xml configuration file.");
		XmlParser xmlParser = new XmlParser( );
		System.out.println("parse done, now produce initial url to message queue.");
		MQProducer initialProducer = new MQProducer(xmlParser.urltodoParameter);
		initialProducer.sendMessage("");
		
		if( !xmlParser.activateDownload){			//master
			Runnable urlFilterConsumer = new URLFilterConsumer(xmlParser.urlfilterParameter);	//activate UrlFilterConsumer
			Thread tu = new Thread(urlFilterConsumer);
			tu.start();
			BloomFilter bloom = new BloomFilter(xmlParser.filter_size);	//initialize Bloom filter
		}else{			//slave
			Runnable urltodoConsumer = new URLTODOConsumer(xmlParser.urltodoParameter);		//activate UrlTodoConsumer
			Thread tu = new Thread(urltodoConsumer);
			tu.start();
			MessagePool messagePool = new MessagePool(10);	//initialize MessagePool
			MessageProcess messageProcess = new MessageProcess(xmlParser.urltodoParameter,		//activate MessageProcess
					xmlParser.dbParameter, xmlParser.threadCount);
			Thread tm = new Thread(messageProcess);
			tm.start();
		}
		
		System.out.println("program out!");
	}
}
