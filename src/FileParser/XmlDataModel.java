///*
//* TODO: 	parse the config.xml file.
//* author:	vito
//* time:		2016-5-7
//*/
//package FileParser;
//
//public class XmlDataModel {
//	//public Xml_Role role;
//	public Xml_Messager urlTODO;
//	public Xml_Messager urlFilter;
//	public Xml_DB pageDB;
//	public Xml_DB filterDB;
//	public Xml_Downloader downloader;
//	
//	public XmlDataModel( ) {
//		//role = new Xml_Role();
//		urlTODO = new Xml_Messager();
//		urlFilter = new Xml_Messager();
//		pageDB = new Xml_DB();
//		filterDB = new Xml_DB();
//		downloader = new Xml_Downloader();
//	}
//	
//	
//	// /**
//	// * set role of this machine
//	// * @param type: configure the role of current machine
//	// * @return
//	// */
//	// public boolean setRole(String type){
//	// if(type==null){
//	// return false;
//	// }
//	// this.role.type = type;
//	// return true;
//	// }
//	/**
//	 * 
//	 * @param name
//	 * @param isActivated
//	 * @param ip
//	 * @param port
//	 * @return
//	 */
//	public boolean setMessageURLTodo(String name, boolean isActivated, String ip, String port){
//		if(name==null || ip== null || port==null){
//			return false;
//		}
//		this.urlTODO.name = name;
//		this.urlTODO.ip = ip;
//		this.urlTODO.port = port;
//		this.urlTODO.isActivated = isActivated;
//		return true;
//	}
//	/**
//	 * set the information of URL filter message queue
//	 * @param name: the MQ name
//	 * @param isActivate: control whether activate on current machine
//	 * @param ip: the target machine that filter MQ running on
//	 * @param port: the port to connect to
//	 * @return status of operation
//	 */
//	public boolean setMessageURLFilter(String name, boolean isActivate, String ip, String port){
//		if(name==null || ip==null || port==null){
//			return false;
//		}
//		this.urlFilter.name = name;
//		this.urlFilter.isActivated = isActivate;
//		this.urlFilter.ip = ip;
//		this.urlFilter.port = port;
//		return true;
//	}
//	/**
//	 * set information of web page database
//	 * @param name: the name of page database
//	 * @param isActivated: control whether activate on current machine
//	 * @param ip: the target machine that web page database running on
//	 * @param port: the port to connect to
//	 * @param database:database name
//	 * @param table:table name
//	 * @return status of operation
//	 */
//	public boolean setPageDB(String name, boolean isActivated, String ip, String port, String database, String table){
//		if(name==null || ip==null || port==null ||
//				database==null || table==null){
//			return false;
//		}
//		this.pageDB.name = name;
//		this.pageDB.isActivated = isActivated;
//		this.pageDB.ip = ip;
//		this.pageDB.port = port;
//		this.pageDB.database = database;
//		this.pageDB.table = table;
//		return true;
//	}
//	/**
//	 * set the information of URL filter database
//	 * @param name: the name of database
//	 * @param isActivated: control whether activate on current machine
//	 * @param ip: the target machine that filter database running on
//	 * @param port: the port to connect to
//	 * @param database: database name
//	 * @param table: table name
//	 * @return status of operation
//	 */
//	public boolean setFilterDB(String name, boolean isActivated, String ip, String port, String database, String table){
//		if(name==null || ip==null || port==null ||
//				database==null || table==null){
//			return false;
//		}
//		this.filterDB.name = name;
//		this.filterDB.isActivated = isActivated;
//		this.filterDB.ip = ip;
//		this.filterDB.port = port;
//		this.filterDB.database = database;
//		this.filterDB.table = table;
//		return true;
//	}
//	/**
//	 * set the information of downloader on slave
//	 * @param count: the count of downloader which will start on slave
//	 * @param unit: the unit of delay
//	 * @param delay: the count of delay
//	 * @return status of operation
//	 */
//	public boolean setDownloader(int count, String unit, int delay, boolean isActivated){
//		if(count<=0 || unit==null || delay<=0){
//			return false;
//		}
//		this.downloader.count = count;
//		this.downloader.unit = unit;
//		this.downloader.delay = delay;
//		this.downloader.isActivated = isActivated;
//		return true;
//	}
//}
//
///// **
//// * @author vito
//// * @category Role configured in the configuration file
//// */
//// class Xml_Role{
//// public String type;
////
//// public Xml_Role(String type){
//// this.type = type;
//// }
////
//// public Xml_Role(){}
//// }
//
//
//
