package io.jumpon.api.library;


import java.io.IOException;
import java.util.Iterator;

/* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;

/**
 *
 * @author Khalid
 */

 
public class Common {
    
    public enum MessageType {        
        Response, Information, Error        
    }
    
    public static String nvl(String value, String alternateValue) {
    if (value == null)
        return alternateValue;

    return value;
    }

	public static boolean isValidURL(String urlStr) {
		
		/*String[] schemes = {"http","https", "chrome", "file", "ftp"}; // DEFAULT schemes = "http", "https", "ftp"
		UrlValidator urlValidator = new UrlValidator(schemes);
		System.err.println("Url:"+urlStr);
		System.err.println(urlValidator.isValid(urlStr));
		System.err.println(Pattern.matches("^[a-z]+://.*", urlStr));*/
		return Pattern.matches("^.*/.*", urlStr);
		
		//return  Pattern.matches("^[a-z]+://*", urlStr);
		
	    /*try {
	      //@SuppressWarnings("unused")
	      //URL url = new URL(urlStr);
	      return true;
	    }
	    catch (MalformedURLException e) {
	        return false;
	    }*/
	}	
	public static String executeGetURLWithParams (String url, List <NameValuePair> params) {  
	
		
		return executeGetURL(url, params, null);
	}

    public static String executeGetURL (String url, List <NameValuePair> params, List <NameValuePair> headers) {  
    	
    	String res = null;
    	
    	try {

	        CloseableHttpClient httpclient = HttpClients.createDefault();
	        	        	        
	        String reqUrl = (params != null)?url +"?" + URLEncodedUtils.format(params, "utf-8"):url;
	        
	        
	        System.err.println("Executing url:"+reqUrl);
	        
	        HttpGet  httpRequest = new HttpGet(reqUrl); 
	        
	        //Add headers if any
	        if (headers != null)  {
	        	
	        	Iterator<NameValuePair> hit = headers.iterator();
	        	 while (hit.hasNext()) {
	        		 
	        		 NameValuePair header = hit.next();
	        		 
	        		 httpRequest.addHeader(header.getName(), header.getValue());
	        		 
	        	 }
	        }
	        
/*	    	Iterator<NameValuePair> pit = params.iterator();
	    	System.err.println("Params:");
	        while(pit.hasNext()) {
	            
	            NameValuePair param = pit.next();
	            
	            System.err.println(param.getName()+":"+param.getValue());                   

	        System.out.println("Executing request: " + httppost.getRequestLine());
	        System.out.println("With headers:"+ httppost.getEntity().toString());
	        System.out.println("With params:"+ EntityUtils.toString(httppost.getEntity()));
	        }*/
	                
	        

	        CloseableHttpResponse httpResponse = httpclient.execute(httpRequest);
	        
	        res = EntityUtils.toString(httpResponse.getEntity());  
    	}     	
        catch (Error | IOException e) {
                        
            System.err.println(e.toString());
            res = "Url execution failed:"+url;
        }
    	
    	return res;
    }    
    
    public static String executeURLWithParams (String url, List <NameValuePair> params, boolean urlEncoding) {
    	
    	String res = null;
    	
    	try {

	        CloseableHttpClient httpclient = HttpClients.createDefault();
	        HttpPost  httpRequest = new HttpPost(url);;  
	        
	        
	        HttpEntity entityParams = null;
	        	        
	        if (!params.isEmpty()) { 
	        	
	        	if (urlEncoding) {
	        		
	        		System.err.println("Encoding..");
	        		entityParams = new UrlEncodedFormEntity(params);
	        				 
	        	} else {
	        		
	        		System.err.println("NOT Encoding..");        		
	        		EntityBuilder entBuilder = EntityBuilder.create().setContentType(ContentType.APPLICATION_FORM_URLENCODED);
	        		
	        		String str = new String();
	        		
	    	    	Iterator<NameValuePair> pit = params.iterator();
	    	    	System.err.println("Params:");
	    	        while(pit.hasNext()) {
	    	            
	    	            NameValuePair param = pit.next();
	    	            
	    	            str = str+"&"+ System.lineSeparator() +param.getName()+"="+param.getValue();
	    	            
	    	            //System.err.println(param.getName()+":"+param.getValue());                   

	    	        }	        		
	        		
	    	        entBuilder.setText(str.substring(1)+ System.lineSeparator());
	    	        System.err.println(str.substring(1));
	        		entityParams = entBuilder.build();
	        	}
	        }
	        
	        httpRequest.setEntity(entityParams);
	        
/*	    	Iterator<NameValuePair> pit = params.iterator();
	    	System.err.println("Params:");
	        while(pit.hasNext()) {
	            
	            NameValuePair param = pit.next();
	            
	            System.err.println(param.getName()+":"+param.getValue());                   

	        System.out.println("Executing request: " + httppost.getRequestLine());
	        System.out.println("With headers:"+ httppost.getEntity().toString());
	        System.out.println("With params:"+ EntityUtils.toString(httppost.getEntity()));
	        }*/
	                
	        

	        CloseableHttpResponse httpResponse = httpclient.execute(httpRequest);
	        
	        res = EntityUtils.toString(httpResponse.getEntity());  
    	}     	
        catch (Error | IOException e) {
                        
            System.err.println(e.toString());
            res = "Url execution failed:"+url;
        }
    	
    	return res;
    	
    }

    
    public static boolean stringContains(String inStr, List<String> strArr) {
    	
    	for(String str: strArr) {
    		System.err.println("==========");
    		System.err.println(str);
    		System.err.println(inStr);
    	    if(str.trim().equals(inStr))
    	       return true;
    	}
    	return false;
    }
    
    
    
    
    //Authentication
/*    


    
   
    public String validateUser(User user, boolean getHash) {

		String validateUrl = (getHash)?"validateandgethash":"validateuser";
		
        List <NameValuePair> params = new ArrayList <>();
        params.add(new BasicNameValuePair("username", user.getUsername()));
        params.add(new BasicNameValuePair("password", user.getPassword()));

        return executeURLWithParams(validateUrl, params);
    	
    }*/
    
}
