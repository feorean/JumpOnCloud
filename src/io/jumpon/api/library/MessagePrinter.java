package io.jumpon.api.library;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class MessagePrinter {

	private HttpServletResponse response;
	
	public void printUnauthorizedError() {
		
		try {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void printError(int error_code, String errorMessage) {
		
		try {
			response.sendError(error_code, errorMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}	
	
    public void printMessage(String message) {

        PrintWriter out = null;
        
        try {
            
                out = response.getWriter();            
        
        }
        catch (IOException e) {
                
            System.err.println("IO Exception: " + e.getMessage());    
        }        
        
        try {
            if ((message != null ) && (out != null)) {

                out.println( message);

            }
        }
        catch (NullPointerException e) {
            System.out.println("Got some nulllllllllllllllllllllllll");
        }
    }
    
    public MessagePrinter(HttpServletResponse res) {
    	
    	response = res;
    	
    }
}
