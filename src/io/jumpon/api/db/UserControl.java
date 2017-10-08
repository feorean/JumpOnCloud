package io.jumpon.api.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import io.jumpon.api.library.DBConnection;
import io.jumpon.api.library.UserSequencer;
import io.jumpon.api.library.encryption.Hasher;

public class UserControl {

	public static boolean checkIfUserRegistred(String email) {
				
		Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
		
		attributeValues.put(":v_ehash", new AttributeValue(Hasher.getSHA1Hash(email)));
		
		
		//Get list
		QueryRequest qReq = new QueryRequest("jumpon.users")
					.withKeyConditionExpression("ehash = :v_ehash")
					.withExpressionAttributeValues(attributeValues);
		
		QueryResult qResult = DBConnection.dynamoDBClient.query(qReq);
		
		//Execute query and get tag (if exists :( )
        Iterator<Map<String, AttributeValue>> iterator = qResult.getItems().iterator();
		
		return iterator.hasNext();
		
	}
	
	public static Long getUserID(String ehash) {
		
		Long res = null;
		
		Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
		
		attributeValues.put(":v_ehash", new AttributeValue(ehash));
				
		//Get list
		QueryRequest qReq = new QueryRequest("jumpon.users")
					.withKeyConditionExpression("ehash = :v_ehash")
					.withExpressionAttributeValues(attributeValues);
		
		QueryResult qResult = DBConnection.dynamoDBClient.query(qReq);
		
		
        Iterator<Map<String, AttributeValue>> iterator = qResult.getItems().iterator();
		
        if (iterator.hasNext()) {
        
        	Map<String, AttributeValue> row = iterator.next();        	            
    		res = Long.parseLong( row.get("uid").getN());
    		
        }
        
		return res;
		
	}
	
	
	public static Long addUser(String email) {
		
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		Long uid = UserSequencer.next();
		
		item.put("ehash", new AttributeValue(Hasher.getSHA1Hash(email)));
		item.put("uid", new AttributeValue().withN(String.valueOf(uid)));
		
		
		
		PutItemRequest putItemRequest = new PutItemRequest("jumpon.users", item);
		DBConnection.dynamoDBClient.putItem(putItemRequest);  
				
		
		return uid;
		
	}
	
	public static boolean deleteUser(String email) {
		
		return true;
		
	}	
	
}
