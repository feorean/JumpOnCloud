package io.jumpon.api.library;

import java.util.HashMap;
import java.util.Map;

public class IndexParser {
	
	
	public static Map<String, String> parseIndex(String index) {
		
		Map<String, String> wordArr = new HashMap<>();
			
		wordArr.put("word", index.substring(0, index.lastIndexOf("_")));
		wordArr.put("thash", index.substring(index.lastIndexOf("_")+1));
		//TypeStripper.stripString(row.get("idx")).split("[_]")[1] ;

		
		return wordArr;
	}

	
	public static String getIndexHash(String index) {
		
		return parseIndex(index).get("thash");
	}
	
	public static String getIndexWord(String index) {
		
		return parseIndex(index).get("word");
	}	
	
}
