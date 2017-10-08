package io.jumpon.api.db;

import com.amazonaws.services.dynamodbv2.model.QueryResult;

import io.jumpon.api.library.TagTypes;


public abstract class TagList {
	
	
    public static QueryResult getTagList(   String  pKey,
            String  tagBeginning,
            int  limit, 
            TagTypes.TagType tagType) {
    		
    		
    		return  TagFinder.selectBeginningWith(pKey, 
    											tagBeginning,  
    											limit, 
    											tagType);
    		
			
			//for (int i=0; i<resultArr.length; i+=2) {
			//	System.err.println(resultArr[i]+":"+resultArr[i+1]);}	
    		
    		//return BatchTagsFetcher.getBatchTags(TagTypes.getKeyName(tagType), TagTypes.getTableName(tagType), (Object[])resultArr);
    }
    	
    

    
}
