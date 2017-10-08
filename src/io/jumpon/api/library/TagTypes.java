package io.jumpon.api.library;

public class TagTypes {
	
	public static enum TagType { GLOBAL, USER, SEARCHENGINE, HISTORY };
	
	private static String resolveTableName(TagType tagType) {
			
		String tableName = null;
		
		switch (tagType) {
		
		case GLOBAL:
			tableName = "jumpon.globaltags";
			break;
		case USER: 
			tableName = "jumpon.usertags";
			break;
		case SEARCHENGINE:
			tableName = "";
			break;
		case HISTORY:
			tableName = "jumpon.userhistory";
			break;
			default: System.err.println("Table type Not defined");
			
		}
		
		return tableName;
	}
	
	public static String getTableName(TagType tagType) {
		
		return resolveTableName(tagType);
	}
	
	public static String getKeyName(TagType tagType) {
		
		String keyName = null;
		
		switch (tagType) {
		
		case GLOBAL:
			keyName = "rg";
			break;
		case USER: 
			keyName = "uid";
			break;
		case SEARCHENGINE:
			keyName = "";
			break;
		case HISTORY:
			keyName = "u";
			break;
			default: System.err.println("Key type Not defined");
			
		}		
		
		return keyName;
	}

	public static String getIndexTableName(TagType tagType) {
		
		String indexName = null;
		
		switch (tagType) {
		
		case GLOBAL:
			indexName = "jumpon.globaltagindex";
			break;
		case USER: 
			indexName = "jumpon.tagindex";
			break;
		case SEARCHENGINE:
			indexName = "";
			break;
		case HISTORY:
			indexName = "";
			break;
			default: System.err.println("Index type Not defined");
			
		}		
		
		return indexName;
	}	
		
	
	public static String getIndexKeyName() {
		
		return "idx";
	}	
	

}
