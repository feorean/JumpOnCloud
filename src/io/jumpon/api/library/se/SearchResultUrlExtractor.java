package io.jumpon.api.library.se;

import org.json.JSONArray;
import org.json.JSONObject;

public class SearchResultUrlExtractor {

	public static String extract (JSONObject searchResponse) {
		
    	JSONArray searchResults = ((JSONObject)searchResponse.get("webPages")).getJSONArray("value");
    	
    	//System.err.println(searchResults.toString());
    	
    	return  ((JSONObject)searchResults.get(0)).getString("url");  		
	}
	
}
