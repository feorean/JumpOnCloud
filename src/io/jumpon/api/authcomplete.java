package io.jumpon.api;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

import io.jumpon.api.db.UserControl;
import io.jumpon.api.library.*;
import io.jumpon.api.library.encryption.Hasher;
import io.jumpon.api.library.session.AuthenticatedSession;
import io.jumpon.api.library.session.UserSession;


/**
 *
 * @author Khalid
 */

public class authcomplete extends HttpServlet {

   
    /**
	 * 
	 */
	
	static final long serialVersionUID = 1L;
 
	static final String authUrl = Globals.getOauthURL();
	static final String client_id = Globals.getClientID();
	static final String client_secret = Globals.getClientSecret();	
	static final String googleApiEmailUrl = Globals.getGoogleApiEmailUrl();
	static final String grant_type="authorization_code";
	
	protected String baseURL;
	
	
	
    public String getToken (String code) {

        List <NameValuePair> params = new ArrayList <>();
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("client_id", client_id));
        params.add(new BasicNameValuePair("client_secret", client_secret));
        params.add(new BasicNameValuePair("redirect_uri", baseURL+"authcomplete"));
        params.add(new BasicNameValuePair("grant_type", grant_type));        

        
        return Common.executeURLWithParams(authUrl, params, true);
    	
    }
    
    public String getEmail (String accessToken) {

        List <NameValuePair> params = new ArrayList <>();
        params.add(new BasicNameValuePair("access_token", accessToken));
        
        return Common.executeGetURLWithParams(googleApiEmailUrl, params);
    	
    }    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


    	MessagePrinter messagePrinter = new MessagePrinter(response);
    	String accessToken = null;
    	String emailResponse = null;
    	
    	String requestUrl = request.getRequestURL().toString();
    	baseURL = requestUrl.substring(0, requestUrl.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
    	
    	
    	try {
    		
	    	String authErrorStatus = request.getParameter("error");  
	    	String authSuccessCode = request.getParameter("code");
	    	
	    	if (authErrorStatus != null) {
	    		
	    		throw new Exception(authErrorStatus);
	    	}
	    	 
	    	
	    	System.err.println("Code:"+authSuccessCode);
	    	System.err.println("Getting token..");

	    	String validationStatus = getToken(authSuccessCode);
	    	System.err.println("Token requested:"+validationStatus);		
	    	
	    	JSONObject validationResponse = new JSONObject(validationStatus);
	    	
	        if (validationResponse.has("access_token")) {
		        
	        	accessToken = validationResponse.get("access_token").toString();
	        	
	        	System.err.println("Access token:"+accessToken);
	        	
	        	System.err.println("**************************");
	        	System.err.println("Getting email..");
	        	emailResponse = getEmail(accessToken);
	        	System.err.println("Response:"+emailResponse);	        	
	        	System.err.println("**************************");	        	
	        	
	        	JSONObject emailResponseJSON = new JSONObject(emailResponse);
	        	Long uid = null;
	        	
	        	 if (emailResponseJSON.has("displayName")) {
	        	
	        		 String email = ((JSONObject)((JSONArray) emailResponseJSON.get("emails")).get(0)).get("value").toString();
	        		 
	        		 String ehash = Hasher.getSHA1Hash(email);
	        		 uid = UserControl.getUserID(ehash);
	        		 
	        		 if (uid == null) {
	        			 
	        			 uid = UserControl.addUser(email);
	        			 System.err.println("NEW User added!!");
	        		 } 

	        		 UserSession userSession = new AuthenticatedSession(response).createNewSession(String.valueOf(uid), 
	        				 										emailResponseJSON.get("displayName").toString());	       	        		 
	        		 
	        		 if (userSession.getSession() == null) {
	        			 
	        			 throw new Exception("Can not create a session!");
	        		 }
	        		 
	 	        	System.err.println("**************************");
		        	System.err.println("Successfully authenticated..");
		        	System.err.println("Welcome:"+emailResponseJSON.get("displayName").toString());
		        	System.err.println("Email:"+email);
		        	System.err.println("USERID:"+uid);
		        	System.err.println("**************************");
		        		        		 
	        	 }
	        }	    	
	    	
	        response.sendRedirect("/");
    	}
    	catch ( Exception e ){
            
            System.err.println("Error in auth:" + e.getMessage() ); 
            messagePrinter.printMessage("AUTH_ERROR");
            
        }
   }

}


