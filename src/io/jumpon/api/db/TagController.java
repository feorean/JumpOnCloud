package io.jumpon.api.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import io.jumpon.api.library.DBConnection;
import io.jumpon.api.library.IndexParser;
import io.jumpon.api.library.Latency;
import io.jumpon.api.library.Log;
import io.jumpon.api.library.TagIndexer;
import io.jumpon.api.library.TagTypes;
import io.jumpon.api.library.TypeStripper;
import io.jumpon.api.library.TagTypes.TagType;
import io.jumpon.api.library.encryption.Hasher;

public abstract class TagController {
	
    private static QueryResult fetchTag(  String  pKeyVal,
			String 	tag, TagTypes.TagType tagType, String columns) {

    		Log.debug("Tag:\""+tag+"\" fetching from:"+pKeyVal+"  Thash:"+Hasher.getSHA1Hash(tag));
    	
    		
			Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
			//System.out.println("Test1_0");
			
			attributeValues.put(":v_pkey", new AttributeValue(pKeyVal));
			attributeValues.put(":v_thash", new AttributeValue(Hasher.getSHA1Hash(tag)));
			
			
			//Get list
			QueryRequest qReq = new QueryRequest(TagTypes.getTableName(tagType))
						.withKeyConditionExpression(TagTypes.getKeyName(tagType)+" = :v_pkey and thash = :v_thash")
						.withExpressionAttributeValues(attributeValues);
			
			
						
			
			if (columns != null ) {
				qReq = qReq.withProjectionExpression(columns);
			}
			
			Log.performance("Select tag start: "+Latency.getTime());
			QueryResult qResult = DBConnection.dynamoDBClient.query(qReq);
			Log.performance("Select tag done: "+Latency.getTime());
			
			Log.performance("Update record count start: "+Latency.getTime());			
			//Update read counts
			if (qResult.getCount()> 0) {
				Log.debug("Found in:"+pKeyVal);
				
	    		AttributeValue rcAttr = qResult.getItems().get(0).get("rc");
	    		
	    		final int currCount = (rcAttr != null)?Integer.valueOf(rcAttr.getN())+1:0;
	    					
	    		final String thash = TypeStripper.stripString(qResult.getItems().get(0).get("thash"))  ;
	    		final String pKeyValf = pKeyVal;
	    		final String tagf = tag;
	    		final TagTypes.TagType tagTypef = tagType;
	    		
	    		 
            	new Thread(new Runnable() {
                    public void run() {

                    	
                    	                    	
                    	TagController.updateTagCounter(pKeyValf, thash, tagTypef, currCount);
        	    		IndexController.updateIndexCounter(pKeyValf, thash, tagf, tagTypef, currCount);			
                    }
            	}).start();	
            	Log.performance("Update record count done: "+Latency.getTime());
			} else {
				Log.debug("Not found in:"+pKeyVal);
			}
    		
			return qResult;

    } 	
	
    public static QueryResult getTag(  String  pKey,
										String 	tag, TagTypes.TagType tagType) {

		return fetchTag(pKey, tag, tagType, null);

    } 	
    
    public static QueryResult getTag(  String  pKey,
										String 	tag, TagTypes.TagType tagType, String columns) {
	
    	return fetchTag(pKey, tag, tagType, columns);

    }     
    
    public static boolean addTagToDB(String  pKey,
								            String  tag,
								            String  url,
								            TagTypes.TagType tagType,
								            Integer rank) {

		
			Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
			
			String thash = Hasher.getSHA1Hash(tag);
			item.put(TagTypes.getKeyName(tagType), new AttributeValue(pKey));
			item.put("thash", new AttributeValue(thash));
			item.put("tag", new AttributeValue(tag));
			item.put("url", new AttributeValue(url));
			item.put("rc", new AttributeValue().withN(rank.toString()));
			
			
			
			PutItemRequest putItemRequest = new PutItemRequest(TagTypes.getTableName(tagType), item);
			DBConnection.dynamoDBClient.putItem(putItemRequest);  
			
			//Add index as well
			if (!IndexController.addTagIndecesToDB(pKey, tag, thash, tagType, rank)) {
				
				Log.error("Problems in adding tag index. See error stack.");
			};
			
			return true; 

    }    


     
    
    
    
    public static boolean deleteTag(	String pKey,
			String tag,
			TagTypes.TagType tagType) {

    		String thash = Hasher.getSHA1Hash(tag);

			HashMap<String, AttributeValue> deleteKey = new HashMap<>();
			deleteKey.put(TagTypes.getKeyName(tagType), new AttributeValue(pKey));
			deleteKey.put("thash", new AttributeValue(thash));
			
			DBConnection.dynamoDBClient.deleteItem(TagTypes.getTableName(tagType), deleteKey);    	
			
			//Delete indeces as well
			IndexController.deleteIndeces(pKey, tag, thash, tagType);
			
			return true;

    }   
    
    public static boolean checkIfTagExists(String pKey, String tag, TagTypes.TagType tagType) {

		QueryResult tagRow = getTag(pKey, tag, tagType);
    	
    	return tagRow.getCount()>0;
    }
    
    public static boolean checkIfTagContains(String pKey, String thash, String tagPortion, TagTypes.TagType tagType) {

		Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
		
		attributeValues.put(":v_pkey", new AttributeValue(pKey));
		attributeValues.put(":v_thash", new AttributeValue(thash));
		attributeValues.put(":v_tagContains", new AttributeValue(tagPortion));
		
		//Get list
		QueryRequest qReq = new QueryRequest(TagTypes.getTableName(tagType))
					.withKeyConditionExpression(TagTypes.getKeyName(tagType)+" = :v_pkey and thash = :v_thash")
					.withFilterExpression("contains ( tag, :v_tagContains )")
					.withExpressionAttributeValues(attributeValues);
		
		QueryResult qResult = DBConnection.dynamoDBClient.query(qReq);
		
		return qResult.getCount() > 0;
    }    
    
    private static List<Map<String, AttributeValue>> _filterTagsContaining(QueryResult batchResult, String[] tagPortions) {
    	
    	List<Map<String, AttributeValue>> foundArr = new ArrayList<>();
    	
    	//Loop fetched tags an look for the string PORTION
	    for (Map<String, AttributeValue> row : batchResult.getItems()) {
	    	
			String tag = TypeStripper.stripString(row.get("tag"));
			
			boolean toAdd = true;
			for (int j=1; (j<tagPortions.length); j++) { 
				//System.err.println("********Checking for word:"+words[j]);
				
				toAdd = tag.contains(tagPortions[j]);
			}
			
	    	if (toAdd) {
	    		foundArr.add(row) ;
	    	}
	    }     	
    	
    	return foundArr;
    }
    
    public static List<Map<String, AttributeValue>> getTagsContaining(String pKeyVal, String[] words, TagTypes.TagType tagType, List<Map<String, AttributeValue>> items, int limit) {

    	
    	List<Map<String, AttributeValue>> foundArr = new ArrayList<>();    	
    	
    	//Bit of sanity check
    	if (words == null || words.length == 0) {
    		return null;
    	}
    	
    	Log.info("Searching for tag in: "+items.size()+" items");
    	
    	int processedCount = items.size();
    	int fromIdx = 0;
    	int toIdx = 0;
    	
    	List<Map<String, AttributeValue>> partialItems = null;
    	
    	//AWS limits batches by 100 items so loop and chop the requests
    	while (toIdx < processedCount ) {
    		
    		// -Loop by 100 
    		// -Fetch 100 jumps in batches
    		// -Check every jump for given words
    		
    		//Overwrite toIdx and find out next portion
    		toIdx = (processedCount>(toIdx+100))
    				?toIdx+100
    				:processedCount;//element count - 1 (as it's index)
    		
    		//System.out.println(String.format("Batch request From %d to %d of %d", fromIdx, toIdx, processedCount));
    		
    		partialItems = items.subList(fromIdx, toIdx);
    	
    		//Prepare batch request
    		String[] batchArr = BatchTagsFetcher.prepareRequesList(pKeyVal, partialItems);
			    
	    	//Execute batch request to get tags
    		QueryResult batchResult = BatchTagsFetcher.getBatchTags(TagTypes.getKeyName(tagType), TagTypes.getTableName(tagType), (Object[])batchArr);
    	
    		//Search occurrence of every word
    		foundArr.addAll(_filterTagsContaining(batchResult, words));
    		
    		
    		//System.out.println(String.format("%d", processedCount));
    		fromIdx = toIdx;
    	}	
    		
    	
    	return foundArr;
    }
    
    
    public static boolean updateTagCounter(String pKeyVal, String tag,
            TagTypes.TagType tagType) {
    	
    	int currCount = 0;
    	
    	String columnName = "rc";
    	
    	QueryResult readCountDB = getTag(pKeyVal, tag, tagType, columnName);

    	if (readCountDB.getCount()>0) {

    		AttributeValue rcAttr = readCountDB.getItems().get(0).get(columnName);
    		if (rcAttr != null) {
    			currCount = Integer.valueOf(rcAttr.getN());
    		}
    		
    	}
    		
		String thash = Hasher.getSHA1Hash(tag);
  	
		updateTagCounter(pKeyVal, thash, tagType, currCount);
    	
    	return true;
    }
    
    public static boolean updateTagCounter(String pKeyVal, String thash,
            TagTypes.TagType tagType, int currRecordCount) {
    	
    	int rc = currRecordCount;    	

    	
		Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		
		key.put(TagTypes.getKeyName(tagType), new AttributeValue(pKeyVal));
		key.put("thash", new AttributeValue(thash));
		
		Map<String, AttributeValueUpdate> item = new HashMap<String, AttributeValueUpdate>();
		
		item.put("rc", new AttributeValueUpdate().withValue(new AttributeValue ().withN(String.valueOf(rc))) );
		
		UpdateItemRequest updateItemRequest = new UpdateItemRequest(TagTypes.getTableName(tagType), key, item);
		DBConnection.dynamoDBClient.updateItem(updateItemRequest);     	
    	
    	
    	return true;
    }    
    
}
