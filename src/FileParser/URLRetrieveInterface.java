package FileParser;

import java.util.List;

public interface URLRetrieveInterface {
	
	/**
	 * retrieve urls from html
	 * 
	 * @return
	 */
	public List<String> retriveURLFromHtml();
	
	/**
	 * retrieve urls from script section
	 * 
	 * @return
	 */
	public List<String> retriveURLFromJS();

}