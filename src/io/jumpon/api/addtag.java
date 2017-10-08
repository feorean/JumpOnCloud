package io.jumpon.api;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.routines.UrlValidator;

import javax.servlet.annotation.*;

import io.jumpon.api.db.TagController;
import io.jumpon.api.library.*;
import io.jumpon.api.library.encryption.Hasher;
import io.jumpon.api.library.session.AuthenticatedSession;
import io.jumpon.api.library.session.UserSession;


/**
 *
 * @author Khalid
 */
@MultipartConfig
public class addtag extends HttpServlet {

   
    /**
	 * 
	 */
	
	static final long serialVersionUID = 1L;
    

	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


    	MessagePrinter messagePrinter = new MessagePrinter(response);
    	//UserSession session = new UserSession(request);
    	
        String tag = request.getParameter("tag");
        String url = request.getParameter("url");
        
        //fix unicode letters
        tag = tag.replaceAll("[^\\x00-\\x7F]", " ");
        
        //if for a bookmark url not provided and only tag persists i.e. tag is url
        url = (url==null)?tag:url;
        boolean overwrite = request.getParameterMap().containsKey("overwrite")?request.getParameter("overwrite").equals("true"):false;
        try {
             
        	UserSession session = new AuthenticatedSession(request);
         	
            //First check if authenticated  
        	session.proceedIfAuthenticated();

        	String uid = ((AuthenticatedSession)session).getUserId();
           
            //System.err.println("Full request:"+request.getQueryString());
            //System.err.println("Adding tag:"+  tag);
            
            //System.err.println("Tag unformatted:"+ request.getQueryString().split("&")[0].split("=")[1]);
            
            //url = (url.length()>4 && url.substring(1, 4)=="http")?url:"http://"+url;
            
            if (uid == null || tag == null || url == null) {

            	messagePrinter.printMessage( 
            								MessageBuilder.buildMessageErr("uid, tag or url is incorrect! tag="+tag+" url="+url) ); 

              return;

            }   
            
            //Encode back (getParameter decodes..) to html format
            //tag = request.getQueryString().split("&")[0].split("=")[1];
            
//System.err.println("Debug03");
            if (!Common.isValidURL(url)) {
            	
            	messagePrinter.printError(HttpServletResponse.SC_BAD_REQUEST, "INVALID_URL");
        		return;
            }
//System.err.println("Debug04");     
            
            //Lowercase tag for consistency
            tag = tag.toLowerCase().trim();
            
    		Log.debug("Add Tag:\""+tag+"\"");
    		Log.debug("Add Thash:"+Hasher.getSHA1Hash(tag));
            
    		
    		//Define the table to serach for jump 
    		//through partition type
    		String partition;
    		TagTypes.TagType tagType;
    		switch (Integer.valueOf(uid)) {
    		
	    		case 0: partition = "G";
	    				tagType = TagTypes.TagType.GLOBAL;
	    				break;
	    		case 1: partition = "UK";
						tagType = TagTypes.TagType.GLOBAL;	
						break;
				default:partition = uid;
						tagType = TagTypes.TagType.USER;	
						break;		
    		}
            
    		//Check if exists send warning
            if ( TagController.checkIfTagExists(partition, tag, tagType) && !overwrite ) {
            	
            	messagePrinter.printError(HttpServletResponse.SC_BAD_REQUEST, "DUPLICATE_TAG");
        		return;            	
            }
             
//System.err.println("Debug05 "+uid);
            boolean insertResult = (TagController.addTagToDB(partition, tag, url, tagType, 0));
            
            if (insertResult) {                       
                messagePrinter.printMessage(MessageBuilder.buildMessageInfo("SUCCESSFULLY_ADDED"));
            } else {
                
                System.out.println("Not inserted!");
            }
        }
        catch ( Exception e ){
            
        	if (e.getMessage() == "NOT_AUTHENTICATED") {
        		
        		messagePrinter.printUnauthorizedError();
        		return;
        	}
        	
            System.err.println("Exception in adding tag: "+tag+" Error:"+ e.getMessage() ); 
            messagePrinter.printMessage("ERROR_IN_ADDTAG");
        }
   }


}
