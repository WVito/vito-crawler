/**
 * this is a common interface for HTML and JavaScript
 * downloaders, the implementations should contain the
 * url and content as basic needs.
 */
package WebPage;

public interface Downloader {
	
	public void requestContent();
	
	public boolean isRequestSuccessful();
	
	public String getContent();
}
