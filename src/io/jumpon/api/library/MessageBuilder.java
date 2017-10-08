package io.jumpon.api.library;

import io.jumpon.api.library.Common.MessageType;

public class MessageBuilder {

	
    public static String buildMessage(MessageType messageType, String message) {
      
        
    	String msg = null;
    	
        try {
            if ((messageType != null) && (message != null )) {

            	msg = MessageWrapper.wrapMessage(messageType, message);

            }
        }
        catch (NullPointerException e) {
            System.out.println("Got some nulllllllllllllllllllllllll");
        }
        
        return msg;
    }

    public static String buildMessageInfo(String message) {
        
        return buildMessage(MessageType.Information, message);
        
    }
    
    public static String buildMessageErr(String message) {
        
        return buildMessage(MessageType.Error, message);
        
    }
    
    MessageBuilder () {
    	
    	
    }
	
}
