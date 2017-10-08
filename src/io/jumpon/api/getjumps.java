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

import io.jumpon.api.db.HistoryTopList;
import io.jumpon.api.db.TagList;
import io.jumpon.api.library.*;
import io.jumpon.api.library.se.SuggestionsConverter;
import io.jumpon.api.library.se.SuggestionsExtractor;
import io.jumpon.api.library.se.WebSearchSuggestions;
import io.jumpon.api.library.session.AnonymousSession;
import io.jumpon.api.library.session.AuthenticatedSession;
import io.jumpon.api.library.session.UserSession;


/**
 *
 * @author Khalid
 */
@MultipartConfig
public class getjumps extends HttpServlet {

   
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

    	
        String tagBeg = request.getParameter("jumpbeg"); 
        String orgBeg = tagBeg;
        String query = request.getParameter("q"); 
        
        tagBeg = (tagBeg == null)?query:tagBeg;
        
        tagBeg = tagBeg.toLowerCase();
        
        Log.debug("query:"+tagBeg);
        
        //for extension requests
        if (query != null &&  query.length()>2 && query.substring(0, 2).equals("j ")) {
        	tagBeg = query.substring(2);
        }
        
        try {
                 
        	
        	String uid = null;
        	String suid = null;
        	
        	if (session instanceof AuthenticatedSession && session.checkIfAuthenticated()) {
	            uid = ((AuthenticatedSession)session).getUserId();
        	} else { 
    			session = new AnonymousSession(request, response).getSession(true);     	    		
    			suid = session.getSessionId();
        	}
        	
            if (/*uid == null ||*/ tagBeg == null || tagBeg == "") {

            	request.getRequestDispatcher("gettoptags").forward(request, response);
            	//messagePrinter.printError(HttpServletResponse.SC_NOT_FOUND , "tag is empty!"); 

              return;

            }  
            
            
            String resJSONstr = null;
            //QueryResult htags = null;
            QueryResult utags = null;
            QueryResult gtags = null;
            
            //Get last 5 accessed 
            //if (uid != null) {
            	
            	//htags = HistoryTopList.getLastUniqueAccessedList(suid, "5");
            //}
            
            if (uid != null) {
	            //Execute query and get tag (if user registered :( )
	            utags = TagList.getTagList(uid, tagBeg, defaultTagListCount, TagTypes.TagType.USER);
	            
	            //Add types
		           utags = SuggestionsTyper.addTypeToQueryResult(utags, TagTypes.TagType.USER);
	        }
            
            //System.out.println("Debug44: Utag count - "+utags.getCount());
            //Check if there are not enough values then fetch some from Globals
            if ( (utags != null && utags.getItems().size() < defaultTagListCount) 
            		|| (utags == null)) {
            	
            	gtags = TagList.getTagList("UK", 
            								tagBeg, 
            								(utags != null)?defaultTagListCount-utags.getItems().size():defaultTagListCount, 
            								TagTypes.TagType.GLOBAL);            	
            	
            	Log.debug("Gtags UK Count:"+gtags.getCount());
            	//Try Global db if not found
            	if (gtags == null || gtags.getCount()==0) 
                	gtags = TagList.getTagList("G", 
							tagBeg, 
							(utags != null)?defaultTagListCount-utags.getItems().size():defaultTagListCount, 
							TagTypes.TagType.GLOBAL);    
            	
            	gtags = SuggestionsTyper.addTypeToQueryResult(gtags, TagTypes.TagType.GLOBAL);
            	
            	/*
            	//Get BING suggestions
            	if (gtags == null || gtags.getCount()==0) {
            		
            		gtags = SuggestionsConverter.convertToQueryResult( SuggestionsExtractor.extract(WebSearchSuggestions.suggest(tagBeg)) );
            		
            		gtags = SuggestionsTyper.addTypeToQueryResult(gtags, TagTypes.TagType.SEARCHENGINE);
            	}
            	*/
            	Log.debug("Gtags G Count:"+gtags.getCount());
            }
            
            
            //System.out.println("Tag beg:"+tagBeg);
            if ((utags == null && gtags == null) || (utags != null && utags.getItems().size()==0 && gtags != null && gtags.getItems().size()==0)) {
            	
            	//throw new Exception("No tags found.");
            	
            } else {
            	
            	if (query==null) {
            		
            		
            		JSONArray arr = (JSONArray)Converters.wrapArraysToJSON(null, true, utags, gtags);
            		
            		JSONObject obj = new JSONObject().put("tags", arr);
            		obj.put("query", orgBeg);
            		
            		resJSONstr = obj.toString();            		
            		
            	} else {
            		
            		JSONArray arr = new JSONArray();
            		
            		arr.put(orgBeg);
            		
            		arr.put((JSONArray)Converters.wrapArraysValuesToJSON(null, "tag", true, utags, gtags));
            		
            		//Add one more level with serach tag
            		
            		resJSONstr = arr.toString();
            		
                    //System.out.println("Debug2 JSON Arr:"+resJSONstr);
            	}
            }
            

            //System.out.println("Tag containing:"+tagBeg);
            Log.debug("Debug4:JSON Array:"+resJSONstr);
            
            if (resJSONstr != null) {
            
            	messagePrinter.printMessage(resJSONstr);
            	
            }
            else {
            	
            	Log.error("Problem in getJump!");
            	
            }
        }
        catch ( Exception e ){
            
        	Log.error("Exception in getting jumpBeg: " + tagBeg + " " + e.getMessage() ); 
            messagePrinter.printMessage("ERROR_IN_RETRIVAL");
        }
   }


}

