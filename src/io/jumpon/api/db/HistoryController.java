package io.jumpon.api.db;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;

import com.amazonaws.services.dynamodbv2.document.BatchGetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;

import io.jumpon.api.library.DBConnection;
import io.jumpon.api.library.IndexParser;
import io.jumpon.api.library.Log;
import io.jumpon.api.library.TagIndexer;
import io.jumpon.api.library.TagTypes;
import io.jumpon.api.library.TypeStripper;
import io.jumpon.api.library.TagTypes.TagType;
import io.jumpon.api.library.encryption.Hasher;

public abstract class HistoryController {
	

	@Async
    public static void addHistoryToDB(String  uid,
								            String  time,
								            String  tag) {

		
			Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
			
			System.err.println("uid:"+uid);
			System.err.println("time:"+time);
			System.err.println("tag:"+tag);
			
			item.put("u", new AttributeValue(uid));
			item.put("s", new AttributeValue().withN(time));
			item.put("t", new AttributeValue(tag));
			
			
			PutItemRequest putItemRequest = new PutItemRequest(TagTypes.getTableName(TagTypes.TagType.HISTORY), item);
			DBConnection.dynamoDBClient.putItem(putItemRequest);  
			
			
			

    }    

    public static QueryResult getLastAccessedListLimit(String uid, String  limit) {

    	//TO DO
    	//- Fix region detection
    	
    	
		Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
		//System.out.println("Test1_0");
		
		attributeValues.put(":v_u", new AttributeValue(uid));
		
		
		//Get list
		QueryRequest qReq = new QueryRequest(TagTypes.getTableName(TagTypes.TagType.HISTORY))
				.withScanIndexForward(false)				
				.withProjectionExpression("t")
				.withKeyConditionExpression("u = :v_u")
				.withExpressionAttributeValues(attributeValues) 
				.withLimit(Integer.valueOf(limit));
		
		QueryResult qResult = DBConnection.dynamoDBClient.query(qReq);
		
		
		return qResult;

    }  	    

    public static QueryResult getLastAccessedTag(String uid, String tag) {


		Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
		//System.out.println("Test1_0");
		
		attributeValues.put(":v_u", new AttributeValue(uid));
		attributeValues.put(":v_t", new AttributeValue(tag));
		
		//Get list
		QueryRequest qReq = new QueryRequest(TagTypes.getTableName(TagTypes.TagType.HISTORY))				
				.withProjectionExpression("s")
				.withKeyConditionExpression("u = :v_u and t = :v_t")
				.withExpressionAttributeValues(attributeValues)
				.withIndexName("u-t-index");
		
		QueryResult qResult = DBConnection.dynamoDBClient.query(qReq);
		
		
		return qResult;

    }      
     
    public static boolean deleteHistoryTag(	String uid,
			String tag) {

    	//Get all tags from history 
    	QueryResult data = HistoryController.getLastAccessedTag(uid, tag);
    	
    	TableWriteItems twis = new TableWriteItems(TagTypes.getTableName(TagTypes.TagType.HISTORY));
    	
    	if (data == null || data.getCount() == 0) {
    		
    		return false;
    		
    	}
    	
    	//Loop and delete in batch
    	for (Map<String, AttributeValue> item : data.getItems()) {
    		
			String s = item.get("s").getN();	    	
	    	
			System.out.println("Deleting: u="+uid+" s="+s+" From:"+twis.getTableName());
			
			twis.addHashAndRangePrimaryKeyToDelete("u", uid, "s", new BigInteger(s));
	    	
			
	    }     		

        //TableKeysAndAttributes tagsTableKeysAndAttributes = new TableKeysAndAttributes(TagTypes.getTableName(TagTypes.TagType.HISTORY));//"jumpon.usertags"
        //tagsTableKeysAndAttributes.addHashAndRangePrimaryKeys("u", "s", keyValues);

        
        
        Log.debug("Making batch get request.");
       
        //DELETE
        DBConnection.dynamoDB.batchWriteItem(twis);    	
    	
	
			
        return true;

    }   
    

	    

    
}
