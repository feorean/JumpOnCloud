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
import io.jumpon.api.library.session.AnonymousSession;
import io.jumpon.api.library.session.AuthenticatedSession;
import io.jumpon.api.library.session.UserSession;


/**
 *
 * @author Khalid
 */

public class logout extends HttpServlet {

   
    /**
	 * 
	 */
	
	static final long serialVersionUID = 1L;
	
	    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	MessagePrinter messagePrinter = new MessagePrinter(response);
    	UserSession session = new AuthenticatedSession(request, response);
    	
    	if (session.getSession() == null) 
    			session = new AnonymousSession(request);

    	
    	if (session.checkIfAuthenticated()) {
    	    		
    		session.terminateSession(); 
    		
    	}
    	
    	String requester = request.getParameter("requester");
    	if (requester != null) {
    		
    		System.err.println(requester);
    		
    		if (requester.equals("chrome.extention")) {
    			    			
    			messagePrinter.printMessage( 
						MessageBuilder.buildMessageInfo("LOGGED_OUT") );
    			
    			System.err.println("Response code:"+response.getStatus());
    			
    		}
    	} else {
    	
    		response.sendRedirect("/");
    	
    	}
            
   }


}
