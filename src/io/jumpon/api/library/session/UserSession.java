package io.jumpon.api.library.session;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.auth.AuthenticationException;



public abstract class UserSession {

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Session session;
	
	public HttpServletRequest getRequest() {
		return request;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	public HttpServletResponse getResponse() {
		return response;
	}


	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public Session getSession() {
		
		return (session == null)? validateSession(): session;
	} 

	public void setSession(Session session) {
		this.session = session;
	}

	public abstract String getSessionId();		
    
    //public abstract UserSession createNewSession(String userId, String displayName);
    
    public abstract Session validateSession ();
    
    
    public abstract void terminateSession();
    
    public void proceedIfAuthenticated() 
            throws AuthenticationException {
            
        if ( validateSession() == null ) { 

            throw new AuthenticationException("NOT_AUTHENTICATED");

        }   
    
    }  
    
    public boolean checkIfAuthenticated() {
            
        return !(validateSession() == null) ;
    	
    }      
    
    public UserSession(HttpServletRequest request, HttpServletResponse response ) {
    	
    	this.setRequest(request);
    	this.setResponse(response);
    }
  
    public UserSession(HttpServletRequest request) {
    	
    	this.setRequest(request);

    }    
    
    public UserSession(HttpServletResponse response ) {
    	
    	this.setResponse(response);
    	
    }




}
