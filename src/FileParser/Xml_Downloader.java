/**
 * @author vito
 * @category downloader on slave configuration
 * time:	2016-5-7
 */
package FileParser;

public class Xml_Downloader{
	public int count;
	public String unit;
	public int delay;
	public boolean isActivated;
	
	public Xml_Downloader(int count, String unit, int delay, boolean isActivated){
		this.count = count;
		this.unit = unit;
		this.delay = delay;
		this.isActivated = isActivated;
	}
	
	public Xml_Downloader(){}
}