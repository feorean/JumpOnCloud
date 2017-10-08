package io.jumpon.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.auth.AuthenticationException;


import io.jumpon.api.library.MessagePrinter;
import io.jumpon.api.library.session.AuthenticatedSession;
import io.jumpon.api.library.session.UserSession;

public class myjumps extends HttpServlet {

   
    /**
	 * 
	 */
	
	static final long serialVersionUID = 1L;
	
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	MessagePrinter messagePrinter = new MessagePrinter(response);
    	UserSession session = new AuthenticatedSession(request);

    	
    	//Check if authenticated
    	try {
			session.proceedIfAuthenticated();
		} catch (AuthenticationException e) {
			
			 
            messagePrinter.printMessage("NOT CONNECTED");
		}
    	
    	
    	
        request.getRequestDispatcher("/WEB-INF/jsp/myjumps.jsp").forward(request, response);
    	
            
            
   }


}