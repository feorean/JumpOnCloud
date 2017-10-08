package io.jumpon.api.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import io.jumpon.api.library.TypeStripper;

public class TagSequencer {

	public static Map<String, String> sequenceTagResultList(QueryResult records, int limit) {
		
	List<Map<String, AttributeValue>> items = records.getItems();
	Map<String, String> res = new HashMap<>();		
	
	
    Iterator<Map<String, AttributeValue>> iterator = items.iterator();
    while (iterator.hasNext() && res.size()<limit) {//Due to we add here two elements
    	        	
    	Map<String, AttributeValue> row = iterator.next(); 

    	//messagePrinter.printMessage(   TypeStripper.stripString(row.get("idx")).split("[_]")[1]   );
    	
    	String indexWord = IndexParser.getIndexWord( TypeStripper.stripString(row.get("idx")) );
    	//if (!res.contains(indexWord)) {
    		//res.add(pKeyVal); 
    	//	res.add(indexWord);
    	//}
    	String indexHash = IndexParser.getIndexHash( TypeStripper.stripString(row.get("idx")) );
    	
    	res.put("word", indexWord);
    	res.put("hash", indexHash);
    }	

    return res;
}		
	
}
