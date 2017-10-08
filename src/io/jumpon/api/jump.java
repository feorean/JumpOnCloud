package io.jumpon.api;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import javax.servlet.annotation.*;

import io.jumpon.api.db.TagController;
import io.jumpon.api.library.*;
import io.jumpon.api.library.session.AnonymousSession;
import io.jumpon.api.library.session.AuthenticatedSession;
import io.jumpon.api.library.session.UserSession;


/** 
 *
 * @author Khalid
 */
@MultipartConfig
public class jump extends HttpServlet {

   
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
    			session = new AnonymousSession(request, response).createNewSession();
    	
        String jump = request.getParameter("q");    
        
        Log.performance("Get jump: "+jump+" "+Latency.getTime());
        
    	//Get JUMP URL
        String url = JumpFetcher.getInstance().build(jump, messagePrinter, session).fetch();
        
        //SEND REDIRECT Response back 
        if (url != null) {
        
        	//messagePrinter.printMessage(resJSONstr);
        	Log.debug("Redirecting to:"+url+" "+Latency.getTime());
        	
        	response.sendRedirect(url);
        	
        }
        else {
        	
        	Log.error("Problem in getUserTag!");
        	
        }

   }


}

