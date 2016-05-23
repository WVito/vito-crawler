package FileParser;

import java.util.List;
import java.util.Vector;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

public class HtmlParserClass implements URLRetrieveInterface, URLFilterInterface, FormatURLsInterface {

	/**
	 * URL of content
	 */
	private final String url;
	/**
	 * HTML to be processed
	 */
	private final String content;
	
	/**
	 * Constructor
	 * need url and it's content
	 * 
	 * @param url
	 * @param content
	 */
	public HtmlParserClass( String url, String content) {
		this.url = url;
		this.content = content;
	}
	
	/**
	 * retrieve URLs from HTML in content
	 * exclude <script > node
	 */
	@Override
	public List<String> retriveURLFromHtml() {
		List<String> urls = new Vector<String>();
		try {
			Parser parser = new Parser(content);
			parser.setEncoding("UTF-8");
			// find nodes with <a href="">
			NodeFilter aFilter = new TagNameFilter("a");
			NodeFilter hrefFilter = new HasAttributeFilter("href");
			AndFilter aWithHrefFilter = new AndFilter(aFilter, hrefFilter);
			NodeList urlList = parser.extractAllNodesThatMatch(aWithHrefFilter);
			if (urlList.size() == 0) {
				return null;
			}
			// get urls from nodes
			for (int i = 0; i < urlList.size(); i++) {
				LinkTag tag = (LinkTag) urlList.elementAt(i);
				urls.add(tag.getAttribute("href"));
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return urls;
	}

	/**
	 * retrieve URLs from JS in content
	 */
	@Override
	public List<String> retriveURLFromJS() {
		List<String> urls = new Vector<String>();
		try{
			Parser parser = new Parser(content);
			parser.setEncoding("UTF-8");
			//find nodes with <script href="">
			NodeFilter scriptFilter = new TagNameFilter("script");
			NodeFilter hrefilter = new HasAttributeFilter("href");
			AndFilter scriptWithHrefFilter = new AndFilter(scriptFilter, hrefilter);
			NodeList urlList = parser.extractAllNodesThatMatch(scriptWithHrefFilter);
			if(urlList.size() == 0){
				return null;
			}
			// get urls from nodes
			for( int i = 0; i < urlList.size(); i++){
				LinkTag tag = (LinkTag)urlList.elementAt(i);
				String url = tag.getAttribute("href");
				if(!url.contains(".js")){
					urls.add(url);
				}
			}
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		
		return urls;
	}

	/**
	 * filter URLs, remove JS and CSS files
	 * <----image files and other type of files---->
	 */
	@Override
	public List<String> filterJsCssUrl(List<String> urls) {
		return filterCss(filterJs(urls));
	}

	/**
	 * remove JS file URLs
	 */
	@Override
	public List<String> filterJs(List<String> urls) {
		if(urls == null) return null;
		List<String> result = new Vector<String>();
		for(int i = 0; i < urls.size(); i++){
			String item = urls.get(i);
			if(!item.contains(".js")){
				result.add(item);
			}
		}
		return result;
	}

	/**
	 * remove CSS file URLs
	 */
	@Override
	public List<String> filterCss(List<String> urls) {
		if(urls == null) return null;
		List<String> result = new Vector<String>();
		for(int i = 0; i < urls.size(); i++){
			String item = urls.get(i);
			if(!item.contains(".css")){
				result.add(item);
			}
		}
		return result;
	}

	/**
	 * complete single URL in full format
	 * this function will detect five kinds of uncompleted URLS:
	 * "./", "/", "../", "./../","//"
	 */
	@Override
	public String formatSingleURL(String url) {
		if(((url.substring(0, 1).charAt(0)>'a')&&(url.substring(0, 1).charAt(0)<'z')) ||
				((url.substring(0, 1).charAt(0)>'A')&&(url.substring(0, 1).charAt(0)<'Z'))){	//normal URL starts with character
			return url;
		}else if(url.startsWith("./")){
			int index = this.url.lastIndexOf("/");
			if(index == -1){	
				return this.url+url.substring(1);
			}else{
				return this.url.substring(0, index) + url.substring(1);
			}
		}else if(url.startsWith("/")){
			int index = this.url.lastIndexOf("/");
			if(index == -1){
				return this.url + url;
			}else{
				return this.url.substring(0, index) + url;
			}
		}else if(url.startsWith("../")){
			int index1 = this.url.lastIndexOf("/");
			if(index1 == -1) return null;
			int index2 = (this.url.substring(0, index1)).lastIndexOf("/");
			if(index2 == -1){
				return null;
			}else{
				return this.url.substring(0, index2) + url.substring(2);
			}
		}else if( url.startsWith("./../")){
			int index1 = this.url.lastIndexOf("/");
			if(index1 == -1) return null;
			int index2 = (this.url.substring(0, index1)).lastIndexOf("/");
			if(index2 == -1){
				return null;
			}else{
				return this.url.substring(0, index2) + url.substring(4);
			}
		}else if( url.startsWith("//")){
			if(this.url.startsWith("http")){
				return this.url.substring(0, this.url.indexOf(":")+1) + url;
			}
		}else {
			System.out.println("format url: " + url + "failed.");
		}
		return null;
	}

	/**
	 * complete URLs in full format
	 */
	@Override
	public List<String> formatURLS(List<String> urls) {
		List<String> resultURLS = new Vector<String>();
		int len = urls.size();
		for(int i = 0; i < len; i++){
			String url = formatSingleURL(urls.get(i));
			if(url == null) continue;
			resultURLS.add(url);
		}
		return resultURLS;
	}
}











