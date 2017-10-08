package io.jumpon.api.library;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

public class SuggestionsTyper {

	
	public static QueryResult addTypeToQueryResult(QueryResult query, TagTypes.TagType tagType) {
		
		if (query == null) 
			return null;
		
		String type = (tagType == TagTypes.TagType.GLOBAL)?"g":(tagType == TagTypes.TagType.USER)?"u":"s";
		
		List<Map<String, AttributeValue>> res = new ArrayList<>();
	
		for (Map<String, AttributeValue> item : query.getItems()) {
			
			item.put("type", new AttributeValue(type));
	    	res.add(item);
		}
		
		return new QueryResult().withItems(res).withCount(res.size());
	}
}
