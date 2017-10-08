package io.jumpon.api;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jumpon.api.library.*;
import io.jumpon.api.library.session.AuthenticatedSession;
import io.jumpon.api.library.session.UserSession;


/**
 *
 * @author Khalid
 */

public class auth extends HttpServlet {

   
    /**
	 * 
	 */
	
	static final long serialVersionUID = 1L;
	
	
    static final String clientID=Globals.getClientID();
    static final String scope=Globals.getGoogleApiEmailScope();    
    static final String state="stablelineofrequest";//Should bw probably unique each time?!
    static final String response_type="code";
      
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	UserSession session = new AuthenticatedSession(request);

    	String requestUrl = request.getRequestURL().toString();
    	String baseURL = requestUrl.substring(0, requestUrl.length() - request.getRequestURI().length()) + request.getContextPath() + "/";

    	
    	String url="https://accounts.google.com/o/oauth2/auth?scope="+scope+"&client_id="+clientID+"&redirect_uri="+baseURL+"authcomplete"+
    		    "&response_type="+response_type+"&state="+state;
    	
    	if (session.checkIfAuthenticated()) {
    	
    		response.sendRedirect("/");
    		
    	} else {
            System.out.println("Redirecting to:"+url);
    		response.sendRedirect(url);
    	}
            
            
   }


}
