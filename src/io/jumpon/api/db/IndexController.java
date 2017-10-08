package io.jumpon.api.db;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import io.jumpon.api.library.DBConnection;
import io.jumpon.api.library.ReadCountComparator;
import io.jumpon.api.library.TagIndexer;
import io.jumpon.api.library.TagTypes;
import io.jumpon.api.library.TypeStripper;
import io.jumpon.api.library.encryption.Hasher;


public abstract class IndexController {

    public static QueryResult getIndexValue(  String  pKeyValue,
												String 	idx, TagTypes.TagType tagType) {
    		
			Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
			
			attributeValues.put(":v_pkey", new AttributeValue(pKeyValue));
			attributeValues.put(":v_idx", new AttributeValue(idx));
			
			
			//Get list
			QueryRequest qReq = new QueryRequest(TagTypes.getIndexTableName(tagType))
						.withKeyConditionExpression(TagTypes.getKeyName(tagType)+" = :v_pkey and idx = :v_idx")
						.withExpressionAttributeValues(attributeValues);
			
			QueryResult qResult = DBConnection.dynamoDBClient.query(qReq);
			
			
			return qResult;

    } 	
	
    public static boolean checkIfIndexValueExists(String pKey, String idx, TagTypes.TagType tagType) {

		QueryResult idxRow = getIndexValue(pKey, idx, tagType);
    	
    	return idxRow.getCount()>0;
    }	
	
    public static boolean addTagIndecesToDB(String  pKey,
            String tag, 
            String thash,
            TagTypes.TagType tagType,
            Integer rank) {

    	boolean res = false;
    	try {                    

            TableWriteItems indexTableWriteItems = new TableWriteItems(TagTypes.getIndexTableName(tagType))
            			.withItemsToPut(TagIndexer.indexTag(TagTypes.getKeyName(tagType), pKey, tag, thash, rank));
            
            //System.out.println("Making the request.");
            BatchWriteItemOutcome outcome = DBConnection.dynamoDB.batchWriteItem(indexTableWriteItems);

            do {

                // Check for unprocessed keys which could happen if you exceed provisioned throughput

                Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();

                if (outcome.getUnprocessedItems().size() == 0) {
                    //System.out.println("No unprocessed items found");
                } else {
                    System.out.println("Retrieving the unprocessed items");
                    outcome = DBConnection.dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
                }

            } while (outcome.getUnprocessedItems().size() > 0);

            res = true;
        }  catch (Exception e) {
            System.err.println("Failed to retrieve items: ");
            e.printStackTrace(System.err);
        }  
    	
		
		return res; 
	
	}   

    /*
    public static QueryResult getIndeces(  String  pKeyValue,
												String 	thash, String tag, TagTypes.TagType tagType) {

		Map<String, AttributeValue> indeces = new HashMap<String, AttributeValue>();
		

		//Get list
		Set<Item> indecItems = TagIndexer.indexTag(TagTypes.getKeyName(tagType), pKeyValue, tag, thash);
	
		for (Item i : indecItems) {
			i.
			indeces.put("idx", value)
		}
		
		
		return qResult;
	
	}   */  
    
    public static boolean updateIndexCounter(String pKeyVal, String thash, String tag,
            								TagTypes.TagType tagType, Integer counter) {
    	

    	String columnName = "rc";
    	
    	//QueryResult indeces = getIndeces(pKeyVal, thash, tagType);

    	//Generate list
    	Set<Item> indeces = TagIndexer.indexTag(TagTypes.getKeyName(tagType), pKeyVal, tag, thash, counter);

    	if (indeces.size()>0) {

    		for (Item index : indeces) {
    			
    			String idx = index.getString("idx");
    			//System.err.println(idx);
	    		Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
	    		
	    		key.put( TagTypes.getKeyName(tagType), new AttributeValue(pKeyVal) );//Part.ion Key 
	    		key.put("idx", new AttributeValue(idx));//Range Key
	    		
	    		Map<String, AttributeValueUpdate> item = new HashMap<String, AttributeValueUpdate>();
	    		
	    		item.put(columnName, new AttributeValueUpdate().withValue(new AttributeValue ().withN(String.valueOf(counter))) );
	    		
	    		UpdateItemRequest updateItemRequest = new UpdateItemRequest(TagTypes.getIndexTableName(tagType), key, item);
	    		
	    		DBConnection.dynamoDBClient.updateItem(updateItemRequest);     	
        	
    		}   		
    		
    	}

    	
    	return true;
    }    
    
    public static boolean deleteIndeces(String  pKey,
            String tag, 
            String thash,
            TagTypes.TagType tagType) {

    	boolean res = false;
    	System.err.println("Delete indeces");
    	
    	Set<Item> idxs = TagIndexer.indexTag(TagTypes.getKeyName(tagType), pKey, tag, thash, 0);

    	String sKey = TagTypes.getIndexKeyName();
    	
    	Iterator<Item> it = idxs.iterator();
    	
    	while (it.hasNext()) {
    		
    		Item item = it.next();
    		
    		
    		
    		HashMap<String, AttributeValue> deleteKey = new HashMap<>();
			deleteKey.put(TagTypes.getKeyName(tagType), new AttributeValue(pKey));
			deleteKey.put(sKey, new AttributeValue(item.get(sKey).toString()));
			
			DeleteItemResult delRes = DBConnection.dynamoDBClient.deleteItem(TagTypes.getIndexTableName(tagType), deleteKey);     		
			
    		System.err.println("KEY:"+TagTypes.getKeyName(tagType)+" "+pKey+" IDX:"+item.get(sKey));
    		
    	}
    	
			/*HashMap<String, AttributeValue> deleteKey = new HashMap<>();
			deleteKey.put(TagTypes.getKeyName(tagType), new AttributeValue(pKey));
			deleteKey.put("idx", new AttributeValue());
			
			DBConnection.dynamoDBClient.deleteItem(TagTypes.getTableName(tagType), deleteKey);    	
			*/
			
			return true;

    }
    
}
