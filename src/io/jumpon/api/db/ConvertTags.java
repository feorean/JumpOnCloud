package io.jumpon.api.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import io.jumpon.api.library.IndexParser;
import io.jumpon.api.library.TypeStripper;



public class ConvertTags {
		
		
		List<String> uniqueProcessedList = new ArrayList<>();
		
		
		private static Map<String, AttributeValue> convertToItem(String key, String value) {
			
			Map<String, AttributeValue> convertedItem = new HashMap<>(); 
			convertedItem.put("thash", new AttributeValue(value));
			    	
			return convertedItem;
			
		}		
		
		public List<Map<String, AttributeValue>> convert (QueryResult unconvertedList) {
			
			List<Map<String, AttributeValue>> convertedItems = new ArrayList<>();
			
		    for (Map<String, AttributeValue> item : unconvertedList.getItems()) {
		    	
				String idx = TypeStripper.stripString(item.get("idx"));
		    	String indexHash = IndexParser.getIndexHash( idx );		    	
	
		    	if (!uniqueProcessedList.contains(indexHash)) {
		    	
		    		convertedItems.add(convertToItem("thash", indexHash));	
	
			    	//We need to maintain list to avoid duplication if paging happens or list have duplicates
		    		uniqueProcessedList.add(indexHash);
		    	}
		    	
	
		    } 
		    
		    return convertedItems;
		}
		
	
}
