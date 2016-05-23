package FileParser;
/**
 * @author vito
 * @category message queue configuration in the file
 * 
 * time:	2016-5-7
 */
public class Xml_Messager{
	public String name;
	public String ip;
	public String port;
	public boolean isActivated;
	
	public Xml_Messager(String name, String ip, String port, boolean isActivated ) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.isActivated = isActivated;
	}
	
	public Xml_Messager(){}
}