package PageDB;

public class MongoDBParameter {
	
	/**
	 * IP of MongoDB running on
	 */
	public String ip;
	
	/**
	 * port to connect to
	 */
	public String port;
	
	/**
	 * database name
	 */
	public String dbName;
	
	/**
	 * collection name
	 */
	public String dbCollectionName;
	
	/**
	 * Constructor
	 * need IP, port, database name and collection name.
	 * 
	 * @param ip
	 * @param port
	 * @param dbName
	 * @param collectionName
	 */
	public MongoDBParameter(String ip, String port, String dbName, String collectionName){
		this.ip = ip;
		this.port = port;
		this.dbName = dbName;
		this.dbCollectionName = collectionName;
	}
}
