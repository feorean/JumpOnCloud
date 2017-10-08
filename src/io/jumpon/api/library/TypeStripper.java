package io.jumpon.api.library;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class TypeStripper {

	public static String stripString(AttributeValue value) {
		
		//System.err.println(value);
		String tmpStr = value.toString();
		String val = tmpStr.substring(tmpStr.indexOf(":")+2, tmpStr.length()-2);
				//.split("S: ")[1].split(",}")[0]; 
		//System.err.println(val);
		return val;
	}
	
	
	
}
