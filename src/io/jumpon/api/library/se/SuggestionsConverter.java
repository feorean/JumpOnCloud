package io.jumpon.api.library.se;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

public class SuggestionsConverter {

	
	public static QueryResult convertToQueryResult(Set<String> suggestions) {
		
		List<Map<String, AttributeValue>> res = new ArrayList<>();
		
		Iterator<String> sIt = suggestions.iterator();
		
		
		
		while (sIt.hasNext()) {
			
			Map<String, AttributeValue> item = new HashMap<>(); 
			item.put("tag", new AttributeValue(sIt.next()));
			
			res.add(item);
			
		}

		return new QueryResult().withItems(res).withCount(res.size());
	}
	
   	
	
}
