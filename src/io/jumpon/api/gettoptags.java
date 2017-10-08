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

import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.services.dynamodbv2.model.QueryResult;
import javax.servlet.annotation.*;

import io.jumpon.api.db.GlobalTopTagList;
import io.jumpon.api.db.HistoryTopList;
import io.jumpon.api.db.UserTopTagList;
import io.jumpon.api.library.*;
import io.jumpon.api.library.session.AnonymousSession;
import io.jumpon.api.library.session.AuthenticatedSession;
import io.jumpon.api.library.session.UserSession;


/**
 *
 * @author Khalid
 */
@MultipartConfig
public class gettoptags extends HttpServlet {

   
    /**
	 * 
	 */
	
	static final long serialVersionUID = 1L;
	static final int defaultTagListCount = 20;
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


    	MessagePrinter messagePrinter = new MessagePrinter(response);
    	UserSession session = new AuthenticatedSession(request);
	
    	
        try {
                 
        	System.err.println("Getting top tags");
            //First check if authenticated  
            //session.proceedIfAuthenticated();
            
        	String uid = null;
        	String suid = null;
        	
        	if (session instanceof AuthenticatedSession && session.checkIfAuthenticated()) {
	            uid = ((AuthenticatedSession)session).getUserId();
	            suid = uid;
        	} else { 
    			session = new AnonymousSession(request, response).getSession(true);     	    		
    			suid = session.getSessionId();
        	}
            
            
            String resJSONstr = null;
            QueryResult htags = null;
            QueryResult utags = null;
            QueryResult gtags = null;
            
            
            if (suid != null) {
            	
            	String sessUID = session.getSessionId(); 
            	
            	System.err.println("SessionID:"+sessUID);
            	
            	htags = HistoryTopList.getLastUniqueAccessedList(suid, "3");
            	
            }
            
            if (uid != null) {
	            //Execute query and get tag (if user registred :( )
	           utags = UserTopTagList.getTopTagList(uid, String.valueOf(defaultTagListCount));
	           
	           //Add types
	           utags = SuggestionsTyper.addTypeToQueryResult(utags, TagTypes.TagType.USER);
            } 
            
            //Check if there are not enough values then fetch some from Globals
            if ( (utags != null && utags.getCount() < defaultTagListCount) 
            		|| (utags == null)) {
            	
            	gtags = GlobalTopTagList.getTopTagList(String.valueOf( (utags != null)?defaultTagListCount-utags.getCount()
            																			:defaultTagListCount));            	
            	gtags = SuggestionsTyper.addTypeToQueryResult(gtags, TagTypes.TagType.GLOBAL);
            }            
            
            if (utags == null && gtags == null) {
            	
            	throw new Exception("No tags found.");
            	
            } else {

                //System.out.println("Debug7:utags.count="+String.valueOf(utags.getCount()));
                //System.out.println("Debug7:gtags.count="+String.valueOf(gtags.getCount()));
            	//resJSONstr = Converters.wrapArraysToJSON(null, true, utags, gtags);
            	
            	JSONArray arr = (JSONArray)Converters.wrapArraysToJSON(null, true, htags, utags, gtags);
            	
        		JSONObject obj = new JSONObject().put("tags", arr);
        		obj.put("query", "empty");
        		
        		resJSONstr = obj.toString();
            }
            
            
            System.out.println("Debug3:JSON Array:"+resJSONstr);
            
            if (resJSONstr != null) {
            
            	messagePrinter.printMessage(resJSONstr);
            	
            }
            else {
            	
            	System.out.println("Problem in getUserTopTags!");
            	
            }
        }
        catch ( Exception e ){
            
            System.err.println("Exception in getting top tags: " + e.getMessage() ); 
            messagePrinter.printMessage("ERROR_IN_RETRIVAL");
        }
   }


}

