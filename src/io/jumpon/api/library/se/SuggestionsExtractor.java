package io.jumpon.api.library.se;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class SuggestionsExtractor {

	
	public static Set<String> extract(JSONObject suggestions) {
		
		Set<String> sList = new HashSet<String>();
		
		
		JSONObject webGroup = ((JSONArray)suggestions.get("suggestionGroups")).getJSONObject(0);
		
		JSONArray suggList = (JSONArray)webGroup.get("searchSuggestions");
		
		Iterator<Object> oIt = suggList.iterator();
		
		while (oIt.hasNext()) {
			
			JSONObject s = (JSONObject)oIt.next();
			
			sList.add(s.getString("displayText"));
			
			//System.err.println(s.getString("displayText"));
		}
		
		return sList;
		
	}
	
}
