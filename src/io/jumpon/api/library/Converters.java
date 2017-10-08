package io.jumpon.api.library;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

public class Converters {

	
	public static String wrapToJSON (String paramName, String value ) {
		
		if (paramName == null || value == null) {
            
            System.out.println("One (or more) of the input parameters is empty!");
            return "";
            
        }
		
		
		JSONObject obj = new JSONObject();
				
		obj.put(paramName, value); 
		
		return obj.toString();
	}
	
	public static boolean jsonArrayContains(JSONArray arr, String value) {
		
        for (int i = 0; i < arr.length(); i++) {
            
        	JSONObject record = (JSONObject)arr.get(i);
        	        	
        	if (record.getString("tag").equals(value)) {
        		return true;
        	}
        	
        }		

		return false;
	}
	
	public static JSONArray concatArray(JSONArray... arrs)
	        throws JSONException {
	    
		
		
		JSONArray result = new JSONArray();
	    for (JSONArray arr : arrs) {
	        for (int i = 0; i < arr.length(); i++) {
	            
	        	if (!jsonArrayContains(result, ((JSONObject)arr.get(i)).getString("tag") )) {
	        		result.put(arr.get(i));
	        	}
	        	
	        	
	        }
	    }
	    return result;
	}
	
	public static boolean jsonArrayContains2(JSONArray arr, String value) {
		
        for (int i = 0; i < arr.length(); i++) {

        	if ( ((String)arr.get(i)).equals(value) ) {
        		return true;
        	}
        	
        }		

		return false;
	}	
	
	public static JSONArray distinctArrays(JSONArray... arrs)
	        throws JSONException {

		JSONArray result = new JSONArray();
	    for (JSONArray arr : arrs) {
	        for (int i = 0; i < arr.length(); i++) {

	        	if (!jsonArrayContains2(result, (String)arr.get(i) )) {
	        		result.put(arr.get(i));
	        	}
	        	
	        	
	        }
	    }
	    return result;
	}
	
	public static JSONArray wrapArrayToJSON (QueryResult records, boolean stripTypes) {
	
        JSONArray jsonArray = new JSONArray();
        
        List<Map<String, AttributeValue>> items = records.getItems();
        
        Iterator<Map<String, AttributeValue>> rowIterator = items.iterator();
        while (rowIterator.hasNext()) {
        	

        	
        	JSONObject obj=new JSONObject();
        	
        	Map<String, AttributeValue> row = rowIterator.next();        	
        	Iterator<String> columns = row.keySet().iterator();
                	           
            while(columns.hasNext()) {
                
                String columnName = columns.next();
                
                obj.put(columnName, (stripTypes?TypeStripper.stripString(row.get(columnName)):row.get(columnName)) );                   

            }

            jsonArray.put(obj);        	
        }		
        //System.out.println("Debug4.1:"+jsonArray.toString());
        return jsonArray;
	}
	
    public static Object wrapArraysToJSON (String arrayName, boolean stripTypes, QueryResult... recordArrays) {

        /*if (arrayName == null) {
            
            System.out.println("Array name is empty!");
            return "";
            
        } */
        
        if (recordArrays == null || recordArrays.length == 0) {
            
            System.out.println("Array is either null or empty!");
            return "";
            
        }
        //System.out.println("Debug4");
        JSONArray jsonArray = new JSONArray();
        
        for ( QueryResult q : recordArrays )    {
        	
        	if (q != null) {
        		jsonArray = concatArray(jsonArray, wrapArrayToJSON(q, stripTypes));
        	}
        }
        	  
        //System.out.println("Debug6-Array:"+jsonArray.toString());
        Object res = jsonArray;
        
        if (arrayName != null) {
        	
        	res=new JSONObject().put(arrayName, jsonArray);
            //res.(JSONObject)put(arrayName, jsonArray);
            
        } 
        	
        return res;
    }
    
	public static JSONArray wrapArrayValuesToJSON (QueryResult records, String columnName, boolean stripTypes) {
		
        JSONArray jsonArray = new JSONArray();
        
        List<Map<String, AttributeValue>> items = records.getItems();
        
        Iterator<Map<String, AttributeValue>> iterator = items.iterator();
        while (iterator.hasNext()) {
        	

        	
        	//JSONObject obj=new JSONObject();
        	
        	Map<String, AttributeValue> row = iterator.next(); 
   
        	jsonArray.put(stripTypes?TypeStripper.stripString(row.get(columnName)):row.get(columnName));                   
      	
        }		
        //System.out.println("Debug4.1:"+jsonArray.toString());
        return jsonArray;
	}    
    
    public static Object wrapArraysValuesToJSON (String arrayName, String columnName, boolean stripTypes, QueryResult... recordArrays) {

        /*if (arrayName == null) {
            
            System.out.println("Array name is empty!");
            return "";
            
        } */
        
        if (recordArrays == null || recordArrays.length == 0) {
            
            System.out.println("Array is either null or empty!");
            return null;
            
        }
        //System.out.println("Debug4");
        JSONArray jsonArray = new JSONArray();
        
        for ( QueryResult arr : recordArrays )    {
        	
        	if (arr != null) {
        		jsonArray = distinctArrays(jsonArray, wrapArrayValuesToJSON(arr, columnName, stripTypes));
        	}
        }
        	  
        //System.out.println("Debug6-Array:"+jsonArray.toString());
        Object res = jsonArray;
        
        if (arrayName != null) {
        	
        	res=new JSONObject().put(arrayName, jsonArray);
            //res.(JSONObject)put(arrayName, jsonArray);
            
        } 
        	
        return res;
    }	
	
    public static JSONArray strToJSONArray(String arrayName, String jsonData) {
        
        JSONObject obj = new JSONObject(jsonData);
        
        JSONArray array = obj.getJSONArray(arrayName);
        
        return array;
    }
    
}
