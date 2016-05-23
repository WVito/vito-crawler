package FileParser;

import java.util.List;

public interface FormatURLsInterface {

	/**
	 * format a single url from web page to full format url
	 * 
	 * @param url
	 * @return
	 */
	public String formatSingleURL(String url);
	
	/**
	 * format urls from web page to full format url
	 * 
	 * @param urls
	 * @return
	 */
	public List<String> formatURLS(List<String> urls);
}
