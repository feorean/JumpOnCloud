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
public class getjump extends HttpServlet {

   
    /**
	 * 
	 */
	
	static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	Log.performance("Get jump: "+Latency.getTime());
    	
    	MessagePrinter messagePrinter = new MessagePrinter(response);
    	UserSession session = new AuthenticatedSession(request);
    	
    	if (session.getSession() == null) 
    			session = new AnonymousSession(request, response).getSession(true); 
    	
        String jump = request.getParameter("q");    
        
    	//Get JUMP URL
        String url = JumpFetcher.getInstance().build(jump, messagePrinter, session).fetch();
        
        String resJSONstr = Converters.wrapToJSON("JUMP", url);
        
        //PRINT - RETURN URL AS JSON STRING
        if (resJSONstr != null) {
        
        	Log.performance("Print jump (getjump): "+Latency.getTime());
        	messagePrinter.printMessage(resJSONstr);
        	
        }
        else {
        	
        	Log.error("Problem in getUserTag!");
        	
        }        
        
        //System.err.println("Full request:"+request.getQueryString());
        //System.err.println("Getting tag:"+tag);
        

   }


}

