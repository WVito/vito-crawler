package FileParser;
/**
 * @author vito
 * @category database configuration
 * time: 2016-5-7
 */
public class Xml_DB{
	public String name;
	public String ip;
	public String port;
	public String database;
	public String table;
	public boolean isActivated;
	
	public Xml_DB(String name, String ip, String port, String database, String table, boolean isActivated){
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.database = database;
		this.table = table;
		this.isActivated = isActivated;
	}
	
	public Xml_DB(){}
}