package io.jumpon.api.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.BatchGetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import io.jumpon.api.library.DBConnection;
import io.jumpon.api.library.TypeStripper;

public class BatchTagsFetcher {

	
	public static String[] prepareRequesList(String pKeyVal, List<Map<String, AttributeValue>> items) {
		
		
		//System.err.println(new QueryResult().withItems(items).toString());
    	//Prepare batch request
    	List<String> hashList = new ArrayList<>();
	    
	    for (Map<String, AttributeValue> item : items) {
	    	
			String thash = TypeStripper.stripString(item.get("thash"));
			//String indexWord = IndexParser.getIndexWord( idx );
	    	//String indexHash = IndexParser.getIndexHash( idx );		    	
	    	
	    	hashList.add(pKeyVal);
	    	hashList.add(thash);
	    }    	
    	
	    //System.err.println(hashList);
	    
		String[] batchArr = new String[hashList.size()];
		batchArr = hashList.toArray(batchArr);
		
		return batchArr;
	}
	
	public static QueryResult getBatchTags(String pKey, String tableName, Object...keyValues) {
		
		List<Map<String, AttributeValue>> resultArr = new ArrayList<>();
		
		String keyName = "thash";
		String columnName = "tag";
		String readCount = "rc";
		try {
            
            TableKeysAndAttributes tagsTableKeysAndAttributes = new TableKeysAndAttributes(tableName);//"jumpon.usertags"
            tagsTableKeysAndAttributes.addHashAndRangePrimaryKeys(pKey, "thash", keyValues)
            .withProjectionExpression(keyName+", "+columnName+", "+readCount);


            //System.out.println("Making batch get request.");
            
           
            BatchGetItemOutcome outcome =  DBConnection.dynamoDB.batchGetItem(tagsTableKeysAndAttributes);
             
            Map<String, KeysAndAttributes> unprocessed = null;  

            do {
                //for (String tableName : outcome.getTableItems().keySet()) {
                    //System.out.println("Items in table " + "jumpon.usertags");
                    List<Item> items = outcome.getTableItems().get(tableName);
                    for (Item item : items) {
                        
                    	//System.out.println(item.toJSONPretty());
                        //System.out.println(item.getNumber(readCount).toString());
                        
                    	Map<String, AttributeValue> val = new HashMap<>();
                    	val.put(keyName, new AttributeValue( (String) item.get(keyName) ));
                    	val.put(columnName, new AttributeValue( (String) item.get(columnName) ));
                    	val.put(readCount, new AttributeValue().withN( item.getNumber(readCount).toString()));
                    	resultArr.add(val);
                    }
               // }
                
                // Check for unprocessed keys which could happen if you exceed provisioned
                // throughput or reach the limit on response size.
                unprocessed = outcome.getUnprocessedKeys();
                
                if (unprocessed.isEmpty()) {
                    //System.out.println("No unprocessed keys found");
                } else {
                    System.out.println("Retrieving the unprocessed keys");
                    outcome = DBConnection.dynamoDB.batchGetItemUnprocessed(unprocessed);
                }
                
            } while (!unprocessed.isEmpty());
 
        }  catch (Exception e) {
            System.err.println("Failed to retrieve items.");
            System.err.println(e.getMessage());
        }  
		//System.err.println(resultArr.toString());
		return new QueryResult().withItems(resultArr).withCount(resultArr.size());
	}
}
