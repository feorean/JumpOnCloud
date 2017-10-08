package io.jumpon.api.library;

import java.util.Comparator;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public  class ReadCountComparator   implements  Comparator<Map<String, AttributeValue>> {
	
	
	@Override
	public  int compare(Map<String, AttributeValue> item1, Map<String, AttributeValue> item2) {
		
		int val1 = Integer.valueOf(item1.get("rc").getN());
		int val2 = Integer.valueOf(item2.get("rc").getN());
		
		return ( val1 == val2 )?0:( val1 > val2 )?-1:1;
	}

}
