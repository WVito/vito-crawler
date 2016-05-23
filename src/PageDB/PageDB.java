package PageDB;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

public class PageDB implements Runnable {
	
	/**
	 * database name
	 */
	private String databaseName;
	
	/**
	 * collection name
	 */
	private String collectionName;
	/**
	 * "URL" field
	 */
	private String url;
	/**
	 * "Content" field
	 */
	private String content;
	
	private Mongo client;
	private DB db;
	private DBCollection collections;
	
	/**
	 * Constructor
	 * store URL and content, URL as "URL" field, content as "Content" field
	 * use bdName and collectionName to configure database
	 * 
	 * @param url	"URL" field
	 * @param content "Content" field
	 * @param dbName database name
	 * @param collectionName collection name
	 */
	public PageDB(String url, String content, String dbName, String collectionName){
		this.url = url;
		this.content = content;
		this.databaseName = dbName;
		this.collectionName = collectionName;
	}
	
	/**
	 * Constructor
	 * store URL and content, URL as "URL" field, content as "Content" field
	 * the database information contains in parameter
	 * 
	 * @param url "URL" field
	 * @param content "Content" field
	 * @param parameter database information
	 */
	public PageDB(String url, String content, MongoDBParameter parameter){
		this.url = url;
		this.content = content;
		this.databaseName = parameter.dbName;
		this.collectionName = parameter.dbCollectionName;
	}
	
	/**
	 * initialize Mongo, DB and DBCollection
	 */
	@SuppressWarnings("deprecation")	//not to check whether method is abandoned
	public void initializeDBConnection(){
		client = new MongoClient();
		db = client.getDB(this.databaseName);
		collections = db.getCollection(this.collectionName);
	}
	
	/**
	 * store "URL" and "Content" into local MongoDB database.
	 */
	public void storeContent(){
		DBObject dbObject = new BasicDBObject();
		dbObject.put("URL", url);
		dbObject.put("Content", content);
		collections.insert(dbObject);
	}
	
	/**
	 * close database connection.
	 */
	public void close(){
		collections = null;
		db = null;
		client.close();
		System.gc();
	}

	/**
	 * override run() of java.lang.Runnable
	 * store content and URL into database.
	 */
	@Override
	public void run() {
		// initialize database connection
		initializeDBConnection();
		// store URL and content
		storeContent();
		// close connection
		close();
	}
}
