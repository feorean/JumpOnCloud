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

import javax.servlet.annotation.*;

import io.jumpon.api.library.*;
import io.jumpon.api.library.Common.MessageType;
import io.jumpon.api.library.session.AnonymousSession;
import io.jumpon.api.library.session.AuthenticatedSession;
import io.jumpon.api.library.session.UserSession;


/**
 *
 * @author Khalid
 */
@MultipartConfig
public class getusername extends HttpServlet {

   
    /**
	 * 
	 */
	
	static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


    	MessagePrinter messagePrinter = new MessagePrinter(response);
    	UserSession session = new AuthenticatedSession(request);
    	
    	if (session.getSession() == null) 
    			session = new AnonymousSession(request, response).getSession(true); 

        try {
        	
	        //First check if authenticated  
	    	session.proceedIfAuthenticated();
	        
	    	String userName = null;
	    	
	    	if (session instanceof AuthenticatedSession && session.checkIfAuthenticated()) {
	    		userName  = ((AuthenticatedSession)session).getDisplayName();
	    	}    	
        

                        
            if (userName == null) {

            	//messagePrinter.printMessage( 
            	//							MessageBuilder.buildMessageErr("User name is incorrect!") ); 

              //return;
            	userName = "NOT_AUTHENTICATED";
            }  
            
            //System.err.println("Return user name:"+MessageBuilder.buildMessageInfo(userName));

            messagePrinter.printMessage(MessageWrapper.wrapMessage_(MessageType.Response, userName, true));
            
        }
        catch ( Exception e ){
            
            System.err.println("Exception in getting username:" + e.getMessage() ); 
            messagePrinter.printUnauthorizedError();
        }
   }


}

