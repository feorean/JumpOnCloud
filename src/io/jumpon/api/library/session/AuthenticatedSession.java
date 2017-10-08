package io.jumpon.api.library.session;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import io.jumpon.api.library.encryption.Encryption;
import io.jumpon.api.library.encryption.Hasher;

public class AuthenticatedSession extends UserSession {


	private JSONObject getJsonSessObj() {
		
		JSONObject sessObj = null;
		
		if (session != null)
			 sessObj = (JSONObject)session.getSessionInfo();
		
		return sessObj;
	}
	
	private String getSessKeyValue(String key) {
		
		JSONObject sessObj = getJsonSessObj();
		
		return (sessObj!= null && sessObj.has(key))?sessObj.getString(key):null;		
		
	}
	
	public String getUserId() {
		
		return getSessKeyValue("uid");
	}
	
	public String getSessionId() {
		
		return getSessKeyValue("sid");
	}	
	
	public String getCreationDate() {
		
		return getSessKeyValue("cdt");
	}	
	
	public String getDisplayName() {
		
		return getSessKeyValue("displayName");
	}		
    
    public AuthenticatedSession createNewSession(String userId, String displayName) {
        
    	
    	try {

        	if (userId == null) {
            	
        		throw new Exception("User id is null");
        		
        	}
        	
    		JSONObject obj = new JSONObject();
	    	
	    	String sessionId = UUID.randomUUID().toString(); 
	    	
	    	String sessionInfo = null; 
	    	
	    	Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
	        
	    	String sessionCreateTime = sdf.format(cal.getTime()) ; 
	
	        obj.put("uid", userId);
	        obj.put("sid", sessionId);
	        obj.put("cdt", sessionCreateTime);
	        obj.put("displayName", displayName);
	    	
	        //System.err.println("Uid value:"+obj.get("uid").toString());
	        
	        //sessionInfo = "{\"uid\":\"13\",\"sid\":\"55516a98-84b1-4674-b88d-78a80095005b\",\"cdt\":\"22/07/2016 22:46:33\"}";
	        //System.err.println("JSON Session:|"+sessionInfo+"|");
	        //Hash session so user can not read it
	        sessionInfo = Hasher.encrypt(obj.toString());	        
	        //sessionInfo = Hasher.encrypt(sessionInfo);
	        System.err.println("JSON Session:"+obj.toString());
	        
	        System.err.println("HASH Session:"+sessionInfo);
	        System.err.println("DECRYPT Session:"+Hasher.decrypt(sessionInfo));
	        if (sessionInfo != null) { 
	        
		        //Sign hashed session info
		        String signature = Encryption.encryptText(sessionInfo);        
		        
		        if (signature != null) {

		        	//System.err.println("Signature:"+signature);
			        
			    	Cookie userCookie = new Cookie("SESSIONID", sessionInfo);
			    	userCookie.setMaxAge(60*60*24*365); //Store cookie for 1 year
			    	response.addCookie(userCookie);
						    	
			    	userCookie = new Cookie("SIGNATURE", signature);
			    	userCookie.setMaxAge(60*60*24*365); //Store cookie for 1 year
			    	response.addCookie(userCookie);
			    	
			    	JSONObject sessObj = ((sessionInfo != null) && (signature != null))?obj:null;
			    	
			    	if (sessObj != null)
			    		session = new SimpleSession(sessObj);
			    	
		        } else {
		        	
		        	throw new Exception("Can not sign session");
		        }
	        } else {
	        	
	        	throw new Exception("Can not generate session info");
	        }
	    }
    	catch (Exception e) {
    		
    		System.err.println("Can not create a session:"+e.getMessage());
    	}
        
    	
    	return this;
    }
    
    public Session validateSession () {
    	
    	String sessionId = null;
    	String signature = null;
    	
    	//Return if already validated
    	if ( session != null ) {
    		
    		return session;
    		
    	}
    	
    	try {
    		
    		Cookie[] cookies = request.getCookies();

    		for (int i = 0; i < cookies.length; i++) {
    		  
    		  //System.err.println("Cookie name:"+cookies[i].getName());	
    		  //System.err.println("Cookie value:"+cookies[i].getValue());
    		  
    		  if (cookies[i].getName().equals("SESSIONID")) {
    			  
    			  sessionId = cookies[i].getValue();
    		  }

    		  
    		  if (cookies[i].getName().equals("SIGNATURE")) {
    			  
    			  signature = cookies[i].getValue();
    		  }    		  
    		  
    		}
    		
    		if ((sessionId == null) || (signature == null)) {
    			
    			throw new Exception("Either session or signature not set");
    		}
    		
    		//System.err.println("Cookie sessionID:"+sessionId);
    		//System.err.println("Cookie signature:"+signature);    		
    		
    		
    		
    		
    		//Validate session
    		
    		if (Encryption.verifySignature(sessionId, signature)) {
    			
    			String sessionInfo = Hasher.decrypt(sessionId);
    			
    			if (sessionInfo != null) {
    				
    				//System.err.println("Decrypted SessionInfo:"+sessionInfo);
    				
    				JSONObject sessObj = new JSONObject(sessionInfo);
    				
    				if (sessObj == null || !sessObj.has("uid")) {
    					
    					throw new Exception("Can not convert session str to Json!");
    				} else {
    					
    					session = new SimpleSession(sessObj);
    				}

    				
    				
    				
    			} else {
    				
    				throw new Exception("Can not decrypt session info!");
    			}
    			
    		} else {
    			
    			throw new Exception("Failed to verify signature!");
    		}
    		
    		
    		
    	} catch (Exception e) {
    		//System.err.println("Can not validate session:"+e.getMessage());
    	}
    	
    	return session;
    }
    
    
    public void terminateSession() {
        
    	//TO DO
        //session.invalidate();  
        
    	Cookie userCookie = new Cookie("SESSIONID", "absolute");
    	userCookie.setMaxAge(0); //Delete cookie
    	response.addCookie(userCookie);
			    	
    	userCookie = new Cookie("SIGNATURE", "absolute");
    	userCookie.setMaxAge(0); //Delete cookie
    	response.addCookie(userCookie);
    	
    }	    

    public AuthenticatedSession(HttpServletRequest request, HttpServletResponse response ) {
    	
    	super(request, response);
    }
  
    public AuthenticatedSession(HttpServletRequest request) {
    	
    	super(request);
    }    
    
    public AuthenticatedSession(HttpServletResponse response ) {
    	
    	super(response);    	
    }	
    
    
}
