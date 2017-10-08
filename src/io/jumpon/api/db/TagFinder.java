package io.jumpon.api.db;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import io.jumpon.api.library.Common;
import io.jumpon.api.library.DBConnection;
import io.jumpon.api.library.IndexParser;
import io.jumpon.api.library.Log;
import io.jumpon.api.library.ReadCountSorter;
import io.jumpon.api.library.SuggestionsTyper;
import io.jumpon.api.library.TagTypes;
import io.jumpon.api.library.TypeStripper;


public class TagFinder {

	private static QueryRequest prepareIndexScanRequest(String pKeyVal, String indexWord, TagTypes.TagType tagType) {
		
		Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
		attributeValues.put(":v_pKeyVal", new AttributeValue(pKeyVal));
		attributeValues.put(":v_tagBegins", new AttributeValue(indexWord));
		
		return  new QueryRequest(TagTypes.getIndexTableName(tagType))
		.withProjectionExpression("idx, rc")
		.withKeyConditionExpression(TagTypes.getKeyName(tagType)+" = :v_pKeyVal and begins_with ( idx, :v_tagBegins ) ")
		.withExpressionAttributeValues(attributeValues);
		
		 //Sorted by word and not Record Count!!!!
				
	}

	

	
	

	
	private static List<Map<String, AttributeValue>> scanIndexAndSearchJumpForWords(String pKeyVal, 
				int limit, 
				String[] words, 
				TagTypes.TagType tagType, 
				QueryRequest qIndexRequest //Select all indeces beginning with a WORD 
				) {		
		
		QueryResult indexScanResult;
		List<Map<String, AttributeValue>> finalFoundTags = new ArrayList<>();
	    
		//Create instance of converter ALSO this will not retrieve duplicate pre-processed thash-es
		ConvertTags convertTags = new ConvertTags();
	    
		int i=0;
		//Handle paging and fetch, merge results
		do {
			i++;
			List<Map<String, AttributeValue>> foundTags = new ArrayList<>();
			
			//Execute & fetch (all - no limit) --Paginations will be done at the end
			//The result is sorted. Thanks to DynamoDb range (sort) key column
			indexScanResult = DBConnection.dynamoDBClient.query(qIndexRequest);
			Log.debug(pKeyVal+" First word index query found count:"+indexScanResult.getCount() + " for "+words[0]+" "+String.valueOf(i));
		    
			//If nothing found then exit
		    if ((indexScanResult != null) && indexScanResult.getCount()==0) {
		    	
		    	return null;
		    }			
						
			//Sort retrieved items by read count to show relevant results first 
			indexScanResult.setItems(ReadCountSorter.sortItems(indexScanResult.getItems()) );
			

			//Convert index values to Tag Hashes 
			//so we can fetch by them
			foundTags = convertTags.convert(indexScanResult);
			
		    if (words.length == 1) {
		    	
		    	//Trim result to avoid unnecessary items
		    	String[] batchRequests = BatchTagsFetcher.prepareRequesList(pKeyVal, 
		    															foundTags.subList(0, (foundTags.size()>limit)?limit:foundTags.size()));
			    
		    	foundTags = BatchTagsFetcher.getBatchTags(TagTypes.getKeyName(tagType), TagTypes.getTableName(tagType), (Object[])batchRequests).getItems();

		    } else {
		    
		    	//For the time being fetch 200 items then come back and fix TO DO
				foundTags = TagController.getTagsContaining(pKeyVal, words, tagType, foundTags, limit * 10);
				
		    }
	    
		    //Collect results for end result
		    finalFoundTags.addAll(foundTags);
			
		    
		    
		    //PAGING
			qIndexRequest = qIndexRequest.withExclusiveStartKey(indexScanResult.getLastEvaluatedKey());						
		} while (indexScanResult.getLastEvaluatedKey() != null 
					&& !indexScanResult.getLastEvaluatedKey().isEmpty() 
					&& (limit > finalFoundTags.size() ));  			
		
		
		
		return finalFoundTags;
	}
	
	public static QueryResult selectBeginningWith(String pKeyVal, String begWith, int limit, TagTypes.TagType tagType) {


		List<Map<String, AttributeValue>> finalFoundTags = new ArrayList<>();			
		
		//Split tag to words e.g. bbc, world, news
		String[] words = begWith.split("[\\s\\/]");
		
    	//Prepare Index scan request NO execution yet
		QueryRequest qIndexRequest = prepareIndexScanRequest(pKeyVal, words[0], tagType);
		
		
		 
		finalFoundTags = scanIndexAndSearchJumpForWords(pKeyVal, limit, words, tagType, qIndexRequest);
			
		
		
		//System.err.println("Found:"+finalFoundTags.size());
		finalFoundTags = ReadCountSorter.sortItems(finalFoundTags);
		//System.err.println("Sorted Found:"+finalFoundTags.size());
		
		List<Map<String, AttributeValue>> subList = finalFoundTags.subList(0, (finalFoundTags.size()>limit)?limit:finalFoundTags.size());
		//System.err.println("Sub:"+subList.size());
		
		QueryResult res = new QueryResult().withItems(subList).withCount(subList.size());
		
		res = SuggestionsTyper.addTypeToQueryResult(res, tagType);
		
		//System.err.println("Result:"+res.getCount());
		//Trim here again for un-trimmed results 
		return res;
	}	
	
}
