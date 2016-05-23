package FileParser;

import java.util.List;

public interface URLFilterInterface {
	
	/**
	 * filter urls of js and css files
	 * 
	 * @return
	 */
	public List<String> filterJsCssUrl(List<String> urls);
	
	/**
	 * just filter urls of js files
	 * 
	 * @return
	 */
	public List<String> filterJs(List<String> urls);
	
	/**
	 * just filter urls of css files
	 * 
	 * @return
	 */
	public List<String> filterCss(List<String> urls);
}
