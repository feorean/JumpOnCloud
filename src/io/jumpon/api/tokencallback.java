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
import org.json.JSONObject;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;

import io.jumpon.api.library.*;


/**
 *
 * @author Khalid
 */

public class tokencallback extends HttpServlet {

   
    /**
	 * 
	 */
	
	static final long serialVersionUID = 1L;
 
	static final String authUrl = "https://www.googleapis.com/oauth2/v4/token";
	static final String client_id="XXXXXXXXXXXX.apps.googleusercontent.com";
	static final String client_secret="XXXXXXXXXXXX";
	static final String redirect_uri="https://oauth2.example.com/code&";
	static final String grant_type="authorization_code";
	
    public String getAccess(String code, boolean getHash) {

        List <NameValuePair> params = new ArrayList <>();
        params.add(new BasicNameValuePair("client_id", client_id));
        params.add(new BasicNameValuePair("client_secret", client_secret));
        params.add(new BasicNameValuePair("redirect_uri", redirect_uri));
        params.add(new BasicNameValuePair("grant_type", grant_type));

        return Common.executeURLWithParams(authUrl, params, false);
    	
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


    	MessagePrinter messagePrinter = new MessagePrinter(response);
    	
    	try {
    		
	    	
	    	  StringBuffer jb = new StringBuffer();
	    	  String line = null;
	    	  JSONObject jsonObject = null;
	    	  
	    	  try {
	    	    BufferedReader reader = request.getReader();
	    	    while ((line = reader.readLine()) != null)
	    	      jb.append(line);
	    	  } catch (Exception e) { /*report an error*/ }

	    	  try {
	    	    jsonObject = new JSONObject(jb.toString());
	    	  } catch (Exception e) {
	    	    // crash and burn
	    	    throw new IOException("Error parsing JSON request string");
	    	  }	   
	    	  
	    	  System.err.println("Result"+jsonObject);
	    	
	    	//JSONObject validationResponse = new JSONObject(validationStatus);
	    	
	    	 
	    	messagePrinter.printMessage( "<h1>RECEIVED TOKEN!!!</h1>" );
	    	//System.err.println("Token:"+authErrorCode);
	    	
    	}
    	catch ( Exception e ){
            
            System.err.println("Error in token retrival:" + e.getMessage() ); 
            messagePrinter.printMessage("TOKEN_ERROR");
            
        }
   }

}


