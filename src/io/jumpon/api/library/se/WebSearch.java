package io.jumpon.api.library.se;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import io.jumpon.api.library.Common;
import io.jumpon.api.library.Globals;

public class WebSearch {

	private static final String seAddress = "https://api.cognitive.microsoft.com/bing/v5.0/search";
	
	public static JSONObject search(String searchTerm, Integer count, Integer offset, String locale, String safesearch) {
		
		if (searchTerm == null || searchTerm.length() == 0) {
			return null;
		}
		
		JSONObject res = new JSONObject(); 
	
        List <NameValuePair> params = new ArrayList <>();
        params.add(new BasicNameValuePair("q", searchTerm));		
        params.add(new BasicNameValuePair("count", String.valueOf(count)));
        params.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        params.add(new BasicNameValuePair("mkt", locale));
        params.add(new BasicNameValuePair("safesearch", safesearch));
        
        List <NameValuePair> headers = new ArrayList <>(); 
        headers.add(new BasicNameValuePair("Ocp-Apim-Subscription-Key", Globals.getBingSearchKey()));	

		String strResult = Common.executeGetURL(seAddress, params, headers);
		
		System.err.println(strResult);
		
		res = new JSONObject(strResult);
		
		return res;
	}
	
	public static JSONObject makeSingleSearch(String searchTerm) {
		
		return search(searchTerm, 1, 0, "en-GB", "Moderate");
	}
	
}
