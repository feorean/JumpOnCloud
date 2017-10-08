package io.jumpon.api.library.se;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import io.jumpon.api.library.Common;
import io.jumpon.api.library.Globals;

public class WebSearchSuggestions {

	private static final String seAddress = "https://api.cognitive.microsoft.com/bing/v5.0/suggestions/";
	
	public static JSONObject suggest(String suggestTerm) {
		
		if (suggestTerm == null || suggestTerm.length() == 0) {
			return null;
		}
		
		JSONObject res = new JSONObject(); 
	
        List <NameValuePair> params = new ArrayList <>();
        params.add(new BasicNameValuePair("q", suggestTerm));	
        
        List <NameValuePair> headers = new ArrayList <>(); 
        headers.add(new BasicNameValuePair("Ocp-Apim-Subscription-Key", Globals.bingSearchSuggestionsKey()));	

		String strResult = Common.executeGetURL(seAddress, params, headers);
		
		//System.err.println(strResult);
		
		res = new JSONObject(strResult);
		
		return res;
	}
	
	
}
