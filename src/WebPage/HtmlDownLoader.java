/**
 * download web page
 * using HttpClient
 * 
 * currently, this class can only acquire pages
 * without user confirm
 * 
 * author:	vito
 * time:	2016-5-8
 * 
 */

package WebPage;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HtmlDownLoader implements Downloader {
	private String url = null;
	private String content = null;
	
	public HtmlDownLoader( String url ){
		this.url = url;
	}

	@Override
	public void requestContent() {
		try{
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>(){
				@Override
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if(status >= 200 && status < 300){
						HttpEntity entity = response.getEntity();
						return entity != null ?
								EntityUtils.toString(entity,"UTF-8"):null;
					}
					return null;
				}
				
			};
			content = httpClient.execute(httpGet, responseHandler);
			httpClient.close();
		}catch(Exception e){
			System.err.println(/*"download html \"" + this.url + " \" failed:" +*/ e.getMessage());
		}
	}

	@Override
	public boolean isRequestSuccessful() {
		if(content != null) return true;
		return false;
	}

	@Override
	public String getContent() {
		return content;
	}
}
