package io.jumpon.api.library;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class ReadCountSorter {

    public static List<Map<String, AttributeValue>> sortItems(List<Map<String, AttributeValue>> indeces) {

		List<Map<String, AttributeValue>> sortedItems = indeces;
		
		Collections.sort(sortedItems, new ReadCountComparator());    	
    	
		return sortedItems;
		
	} 	
	
}
