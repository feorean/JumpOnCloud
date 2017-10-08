package io.jumpon.api.library;

import java.util.HashSet;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.document.Item;

public class TagIndexer {
	
    public static Set<Item> indexTag(String keyName, String  pKey, String tag, String thash, Integer rank) {
    	
    	String[] words = tag.split("[\\s\\/]");
    	Set<Item> res = new HashSet<Item>();
    	//System.err.println("Tag:"+tag);
    	for (int i = 0; i < words.length; i++) {
    	    // You may want to check for a non-word character before blindly
    	    // performing a replacement
    	    // It may also be necessary to adjust the character class
    		//System.err.println(words[i]);
    		String val = words[i];//.replaceAll("[^\\w]", "");
    		//System.err.print("val:"+val);
    		
    		if ( (val.length() > 0 && words.length == 1)  || ( val.length() > 1 )) {
    			//System.err.println("adding:"+val);
    			res.add(new Item()
                    .withPrimaryKey(keyName, pKey, "idx", val+"_"+thash).with("rc", rank));
    		}
    		
    	}
    	
    	return res;
    	
    }	

}
