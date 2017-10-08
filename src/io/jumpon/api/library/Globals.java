package io.jumpon.api.library;

import javax.servlet.ServletContext;

public class Globals {

	
    //Declarations
    private static String awsCredentials = null;
    private static String oauthURL = null;
    private static String clientID = null;
    private static String clientSecret = null;
    private static String googleApiEmailUrl = null;
    private static String googleApiEmailScope = null;  
    private static String bingSearchKey = null;
    private static String bingSearchSuggestionsKey = null;
    
    private static String logLevel = null;
        
    public enum MessageType { 
    	
        Information, Error    
        
    }    
  
    public static String getLogLevel() {    	
    	
        return logLevel;      	
    }        
    
    public static String getAWSCredentials() {    	
    	
        return awsCredentials;      	
    }     
    
    public static String getOauthURL() {    	
    	
        return oauthURL;      	
    } 
    
    public static String getClientID() {    	
    	
        return clientID;      	
    }    
    
    public static String getClientSecret() {    	
    	
        return clientSecret;      	
    }  
    
    public static String getGoogleApiEmailUrl() {    	
    	
        return googleApiEmailUrl;      	
    } 

    public static String getGoogleApiEmailScope() {    	
    	
        return googleApiEmailScope;      	
    } 
    
    public static String getBingSearchKey() {    	
    	
        return bingSearchKey;      	
    }     
    
    public static String bingSearchSuggestionsKey() {
    	
    	return bingSearchSuggestionsKey;
    }
    
    
    public static void loadContextData(ServletContext sc) {
    	
    	logLevel = sc.getInitParameter("logLevel");
    	
    	awsCredentials = sc.getInitParameter("aws_credentials_location");
    	oauthURL = sc.getInitParameter("oauthURL");
    	clientID = sc.getInitParameter("clientID");
    	clientSecret = sc.getInitParameter("clientSecret");
    	googleApiEmailUrl = sc.getInitParameter("googleApiEmailUrl");
    	googleApiEmailScope = sc.getInitParameter("googleApiEmailScope");
    	bingSearchKey = sc.getInitParameter("bingSearchKey");
    	bingSearchSuggestionsKey = sc.getInitParameter("bingSearchSuggestionsKey");
    }
    
}
