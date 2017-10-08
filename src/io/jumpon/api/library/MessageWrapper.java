package io.jumpon.api.library;

import io.jumpon.api.library.Common.MessageType;

public class MessageWrapper {

	public static String wrapMessage(MessageType messageType, String message) {
		
		return wrapMessage_(messageType, message, true);
	}
	
	public static String wrapMessage_(MessageType messageType, String message, boolean withBrackets) {
	
		char begBracket = '{';
		char endBracket = '}';
		
        if (!withBrackets) {
        	begBracket = ' ';
        	endBracket = ' ';
        }

        String tmp = null;
        String result = null;

        try {        
        //System.out.println("Message type:"+messageType+" "+message);
        switch (messageType) {
        case Response:
            tmp = "RES";
            break;          
            case Information: 
                tmp = "INFO";
                break;
            case Error:
                tmp = "ERR";
                break;              
            default: {
                System.out.println("Wrong message type:"+messageType+" "+message);
                break; 
            }
        }

        if (tmp != null) {
            
            result = begBracket+"\""+tmp+"\":\""+Common.nvl(message, " ")+"\""+endBracket;
            
        } else {
                       
            System.out.println("result is null"); 
            
        }

       }
        
        catch (NullPointerException e) {
            System.out.println("Got some nulllllllllllllllllllllllll222222222");
        }        
        
        return result;
    }
  	
	
}
