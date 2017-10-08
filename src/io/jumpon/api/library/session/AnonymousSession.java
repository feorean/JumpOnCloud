package io.jumpon.api.library.session;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AnonymousSession extends UserSession {


	
	public String getSessionId() {
		
		return (String)session.getSessionInfo();
	}	
	
	public UserSession getSession(boolean createIfNotExists) {
		
		Session currSess = validateSession();
		
		if (createIfNotExists && currSess == null)
			createNewSession();
		
	    return this;
	}
	
    
    public AnonymousSession createNewSession() {
        
    	String sessionId = UUID.randomUUID().toString(); 
    	
    	session = new SimpleSession(sessionId);
    	
    	Cookie userCookie = new Cookie("USESSIONID", sessionId);
    	userCookie.setMaxAge(60*60*24*365); //Store cookie for 1 day
    	response.addCookie(userCookie);
    	
    	return this;
    }
    
    public Session validateSession () {
    	
    	//Return if already validated
    	if ( session != null ) {
    		
    		return session;
    		
    	}

    	
    	try {
    		
    		Cookie[] cookies = request.getCookies();

    		for (int i = 0; i < cookies.length; i++) {
    		  
    		  //System.err.println("Cookie name:"+cookies[i].getName());	
    		  //System.err.println("Cookie value:"+cookies[i].getValue());
    		  
    		  if (cookies[i].getName().equals("USESSIONID")) {
    			  
    			  String sessValue = cookies[i].getValue();
    			  
    			  if (!sessValue.equals("absolute"))
    				  session = new SimpleSession( cookies[i].getValue() );
    		  }

    		}

    		
    	} catch (Exception e) {
    		//System.err.println("Can not validate session:"+e.getMessage());
    	}
    	
    	return session; 
    }
    
    
    public void terminateSession() {
        
    	//TO DO
        //session.invalidate();  
        
    	Cookie userCookie = new Cookie("USESSIONID", "absolute");
    	userCookie.setMaxAge(0); //Delete cookie
    	response.addCookie(userCookie);
    	
    }	    
         
    
  
    public AnonymousSession(HttpServletRequest request, HttpServletResponse response ) {
    	
    	super(request, response);
    	

    }
  
    public AnonymousSession(HttpServletRequest request) {
    	
    	super(request);
    	
    }    
    
    public AnonymousSession(HttpServletResponse response ) {
    	
    	super(response);    

    }	    
}
