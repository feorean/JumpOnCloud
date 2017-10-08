package io.jumpon.api.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import io.jumpon.api.library.DBConnection;
import io.jumpon.api.library.IndexParser;
import io.jumpon.api.library.TypeStripper;

public class HistoryTopList {
	


    public static QueryResult getLastUniqueAccessedList(String uid, String  limit) {
    	
    	QueryResult data = HistoryController.getLastAccessedListLimit(uid, limit);
    	
    	if (data == null) {
    		
    		return null;
    		
    	}
    	
    	List<String> uniqueTagList = new ArrayList<>();
    	List<Map<String, AttributeValue>> foundTags = new ArrayList<>();
    	int i = 0;
    	
    	
    	//Loop Rename and remove duplicates using cache array
    	for (Map<String, AttributeValue> item : data.getItems()) {
    		
			String tag = item.get("t").getS();	    	

	    	if (!uniqueTagList.contains(tag) && i<Integer.valueOf(limit)) {

		    	Map<String, AttributeValue> convertedItem = new HashMap<>(); 
		    	convertedItem.put("tag", new AttributeValue(tag));
		    	convertedItem.put("type", new AttributeValue("h"));
		    	foundTags.add(convertedItem);

		    	
		    	uniqueTagList.add(tag);
		    	i++;
	    	}
	    	

	    } 	
	    
    	
		QueryResult res = new QueryResult().withItems(foundTags).withCount(foundTags.size());
	    
		return res;
    }
    
    
}
