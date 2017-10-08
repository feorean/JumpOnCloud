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

import io.jumpon.api.db.HistoryController;
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
public class deletejump extends HttpServlet {

   
    /**
	 * 
	 */
	
	static final long serialVersionUID = 1L;
    

	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


    	MessagePrinter messagePrinter = new MessagePrinter(response);
   	
        String tag = request.getParameter("jump");
        String type = request.getParameter("t");        
        
        System.err.println("DELETE:"+tag+" type:"+type);
        
        //fix unicode letters
        tag = tag.replaceAll("[^\\x00-\\x7F]", " ");
        
        try {
             
        	UserSession session = new AuthenticatedSession(request);
        	
        	if (session.getSession() == null) 
        			session = new AnonymousSession(request);

         	
            //First check if authenticated  
        	session.proceedIfAuthenticated();

        	String uid = null;
        	
        	//No need to check if it's instance of AuthSess. It must be!
        	uid = ((AuthenticatedSession)session).getUserId();
        	
            

            if (uid == null || tag == null ) {

            	messagePrinter.printMessage( 
            								MessageBuilder.buildMessageErr("uid or tag is incorrect! tag="+tag) ); 

              return;

            }   
            
            
            //Lowercase tag for consistency
            tag = tag.toLowerCase();
            
/*            TagTypes.TagType tagType = TagTypes.TagType.GLOBAL;
            
            if ( !uid.equals("1") ) {
            	
            	tagType = TagTypes.TagType.USER;
            	
            }*/
            boolean deleteResult = false;
            
            
            if (type.equals("h")) {	
            	//Delete history
            	deleteResult = HistoryController.deleteHistoryTag(uid, tag);
            } else {
            	
            	deleteResult = ((!uid.equals("1") && TagController.deleteTag(uid, tag, TagTypes.TagType.USER))
						|| ( (uid.equals("1") && TagController.deleteTag("UK", tag, TagTypes.TagType.GLOBAL))
						   && (uid.equals("1") && TagController.deleteTag("G", tag, TagTypes.TagType.GLOBAL))));
            }
            
            if (deleteResult) {                       
                messagePrinter.printMessage(MessageBuilder.buildMessageInfo("SUCCESSFULLY_DELETED"));
            } else {
                
                System.out.println("Not deleted!");
            }
        }
        catch ( Exception e ){
            
        	if (e.getMessage() == "NOT_AUTHENTICATED") {
        		
        		messagePrinter.printUnauthorizedError();
        		return;
        	}
        	
            System.err.println("Exception in deleting jump: "+tag+" Error:"+ e.getMessage() ); 
            messagePrinter.printMessage("ERROR_IN_DELETEJUMP");
        }
   }


}
