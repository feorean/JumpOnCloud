package io.jumpon.api.db;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import io.jumpon.api.library.DBConnection;

public class GlobalTopTagList {
	
    public static QueryResult getTopTagList(String  limit) {

    	//TO DO
    	//- Fix region detection
    	
    	
		Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
		//System.out.println("Test1_0");
		
		attributeValues.put(":v_rg", new AttributeValue("UK"));
		
		
		//Get list
		QueryRequest qReq = new QueryRequest("jumpon.globaltags")
				.withIndexName("idx_rg_rc")
				.withScanIndexForward(false)				
				.withProjectionExpression("tag")
				.withKeyConditionExpression("rg = :v_rg")
				.withExpressionAttributeValues(attributeValues) 
				.withLimit(Integer.valueOf(limit));
		
		QueryResult qResult = DBConnection.dynamoDBClient.query(qReq);
		
		
		return qResult;

    }  	

}
