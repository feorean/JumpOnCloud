package io.jumpon.api.db;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import io.jumpon.api.library.DBConnection;

public class LastUser {

	//*****TO DO (MUST)******
	//Pagination for scan must be done.
	//Dynamo db only retrieves 1 MB data.
	//Or we can save latest known maxuid and reuse for scan filter
	
	public static Long getLastUserID() {
		
		long maxValue = 0L;
		
		//Get list
		ScanRequest qReq = new ScanRequest("jumpon.users")
					.withProjectionExpression("uid")
					;
		
		ScanResult sResult = DBConnection.dynamoDBClient.scan(qReq);
		
		
		List<Map<String, AttributeValue>> items = sResult.getItems();
        
        Iterator<Map<String, AttributeValue>> iterator = items.iterator();
        while (iterator.hasNext()) {
        	
        	Map<String, AttributeValue> row = iterator.next();        	            
        	long currVal = Long.parseLong( row.get("uid").getN());
        	
        	maxValue = (currVal > maxValue)?currVal:maxValue;
        		                
        }		
		
        return Long.valueOf(maxValue);
        
	}
	
}
