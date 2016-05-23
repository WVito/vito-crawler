package Controllers;

import java.util.ArrayList;

public class MessagePool {
	
	//can also be volatile
	private static ArrayList<String> messageList;
	private static int maxLen = 0;
	
	/**
	 * default constructor, the max size is 10
	 */
	public MessagePool() {
		if (messageList == null) {
			messageList = new ArrayList<>();
			maxLen = 10;
		}
	}

	/**
	 * self-defined max size
	 * 
	 * @param maxLen
	 */
	public MessagePool(int maxLen) {
		if (messageList == null) {
			messageList = new ArrayList<>();
			MessagePool.maxLen = maxLen;
		}
	}

	/**
	 * push message to the message pool, if there is more than 10 items in the
	 * pool, the operation will fail.
	 * 
	 * @param message
	 * @return the operation status
	 */
	public static synchronized boolean push(String message) {
		if (message == null)
			return false;
		if (getLength() >= maxLen)
			return false;
		messageList.add(message);
		return true;
	}

	/**
	 * pop out the first message, this operation will delete it after pop out.
	 * 
	 * @return the first item of the list
	 */
	public static synchronized String pop() {
		if (getLength() == 0) {
			System.out.println("there is no more item in the pool.");
			return null;
		}
		String message = messageList.get(0);
		messageList.remove(0);
		return message;
	}

	/**
	 * get the current first item of the pool, this operation will not delete
	 * it.
	 * 
	 * @return
	 */
	public static synchronized String getTheFirst() {
		if (getLength() == 0) {
			System.out.println("there is 0 item in the pool");
			return null;
		}
		String message = messageList.get(0);
		return message;
	}

	/**
	 * get the count of messages in list
	 * 
	 * @return
	 */
	public static synchronized int getLength() {
		return messageList.size();
	}

	/**
	 * check the status of list,
	 * if allow to add item, return true
	 * if not, return false;
	 * 
	 * @return
	 */
	public static synchronized boolean canAdd() {
		if (messageList.size() < maxLen)
			return true;
		return false;
	}
}
