package io.jumpon.api.library;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import io.jumpon.api.db.HistoryController;
import io.jumpon.api.db.TagController;
import io.jumpon.api.library.se.SearchResultUrlExtractor;
import io.jumpon.api.library.se.WebSearch;
import io.jumpon.api.library.session.AuthenticatedSession;
import io.jumpon.api.library.session.UserSession;

public class JumpFetcher {

	static final String searchUrl =  "https://www.google.co.uk/search?q=JUMP&rct=j";
	
	MessagePrinter messagePrinter;
	UserSession session;
	
    String tag = null;    
    String url = null;	
	
    public static JumpFetcher getInstance() {
    	
    	return new JumpFetcher();
    	
    }    
    
    public JumpFetcher build(String jump, MessagePrinter mp, UserSession sess) {
    	
    	tag = (jump != null)?jump.toLowerCase().trim():null;
    	messagePrinter = mp;
    	session = sess;
    	
    	return this;
    }
    
	public String fetch() {
		
        try {
                 
        	Log.performance("Fetching jump:"+tag+" "+Latency.getTime());
        	
        	//Default behaviour if ends with "?": GOOGLE search
        	if (tag.endsWith("?")) {
        		
        		return searchUrl.replace("JUMP", tag.substring(0, tag.length()-1));
        	}
        	//end         	
        	
        	
            //First check if authenticated  
        	String uid = null;
        	String suid = null;
        	
        	
        	if (session instanceof AuthenticatedSession && session.checkIfAuthenticated()) {
	            uid = ((AuthenticatedSession)session).getUserId();
        	} else {
        		
        		suid = session.getSessionId();
        	}
        	Log.performance("Auth done: "+Latency.getTime());
            
            
            if (/*uid == null ||*/ tag == null) {

            	messagePrinter.printMessage( 
            								MessageBuilder.buildMessageErr("uid or tag is incorrect!") ); 

              return null;

            }  
            
            Iterator<Map<String, AttributeValue>> iterator = null;
            if (uid != null) {
	            
            	Log.performance("getTag start: "+Latency.getTime());
            	
            	iterator = TagController.getTag(uid, tag, TagTypes.TagType.USER).getItems().iterator();
            	
            	Log.performance("getTag done: "+Latency.getTime());
            	
	        } 
            

            if ((uid == null) || (iterator == null) || (!iterator.hasNext())) {
	        	
	        	iterator = TagController.getTag("UK", tag, TagTypes.TagType.GLOBAL).getItems().iterator();
	        	
	        	if (!iterator.hasNext()) 
	        		iterator = TagController.getTag("G", tag, TagTypes.TagType.GLOBAL).getItems().iterator();
	        }
            
            if (iterator.hasNext() ) {
            	
            	url = iterator.next().get("url").getS();
            	
            } else {
            	
            	/*
            	//BING
            	//Make BING search request and extract first URL from the result
            	JSONObject searchResponse = WebSearch.makeSingleSearch(tag); 
            	url = SearchResultUrlExtractor.extract(searchResponse);
            	
            	System.err.println("BING response URL:"+url);
            	
            	//Fix/add the protocol
            	url = (url.length()>4 && (url.substring(0, 4).equals("http") || url.substring(0, 3).equals("ftp")) )?url:"http://"+url;
            	
            	System.err.println("Fixed URL:"+url);
            	
            	
            	//ASYNCH call to save this tag for future calls
            	new Thread(new Runnable() {
            	    public void run() {
            	    	TagController.addTagToDB("G", tag, url, TagTypes.TagType.GLOBAL, 0 );
            	    }
            	}).start();
            	//***ASYNCH END
            	
            	//END BING
            	
            	*/
            	
            	//Default GOOGLE search
            	if (url == null)
            		url = searchUrl.replace("JUMP", tag);
            	
            }
            
            if (url != null) {
            	
            	Log.performance("Add history start: "+Latency.getTime());
            	
            	final String uidf = uid;
            	final String suidf = suid;
            	new Thread(new Runnable() {
                    public void run() {

                    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    	Date now = new Date();
                    	//System.out.println(sdf.format(now));
                    	                    	
                    	HistoryController.addHistoryToDB((uidf==null)?suidf:uidf, sdf.format(now), tag);			
                    }
            	}).start();
            	
            	Log.performance("Add history done: "+Latency.getTime());
            }
            

        }
        catch ( Exception e ){
            
        	if (e.getMessage() == "NOT_AUTHENTICATED") {
        		
        		messagePrinter.printUnauthorizedError();
        		return null;
        	}        	
        	
        	Log.error("Exception in getting jump: " + tag + " " + e.getMessage() ); 
            messagePrinter.printMessage("ERROR_IN_RETRIVAL");
        }	
        
        Log.performance("Fetch jump done: "+Latency.getTime());
        return url;
        
	}
	
	
}
