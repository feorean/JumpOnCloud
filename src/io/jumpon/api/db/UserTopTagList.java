package io.jumpon.api.db;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import io.jumpon.api.library.DBConnection;

public class UserTopTagList {
	
    public static QueryResult getTopTagList(   String  uid,
            String  limit) {

		Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
		//System.out.println("Test1_0");
		
		attributeValues.put(":v_uid", new AttributeValue(uid));
		
		
		//Get list
		QueryRequest qReq = new QueryRequest("jumpon.usertags")
				.withIndexName("idx_uid_rc")
				.withScanIndexForward(false)
				.withProjectionExpression("tag")
				.withKeyConditionExpression("uid = :v_uid")
				.withExpressionAttributeValues(attributeValues) 
				.withLimit(Integer.valueOf(limit));
		
		QueryResult qResult = DBConnection.dynamoDBClient.query(qReq);
		
		
		return qResult;

    }  	

}
