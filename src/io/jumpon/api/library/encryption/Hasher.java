package io.jumpon.api.library.encryption;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class Hasher {

	static final byte[] iv = Base64.decodeBase64("XgHXXXXXXXXXXXXXXXXXXXXXXXX=");
	
	static SecretKey secret = getKey();
	
	public static String getSHA1Hash(String value) {
	
		//@SuppressWarnings("deprecation")
		String res = DigestUtils.sha1Hex(value);
		
    	return res;
	}
	
	
	
    private static SecretKey getKey() {
        
    	SecretKey secret = null;
    	try {
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        KeySpec spec = new PBEKeySpec("*****************".toCharArray(), 
	        							  "*****************".getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        secret = new SecretKeySpec(tmp.getEncoded(), "AES");        
    	}
        catch (Exception e) {
        	
        	System.err.println(e.getMessage());
        }
        
        return secret;
    }   
    
    public static String encrypt(String value)  {
    	
    	String res = null;
    	byte[] ciphertext = null;
    	try {
    			//System.out.println("Input text:"+value);
	    		/* Encrypt the message. */
	    		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    		cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));	
	    		
	    		System.err.println(Base64.encodeBase64String(iv));
	    		ciphertext = cipher.doFinal(value.getBytes("UTF-8"));
	    		
		    	
		    	res = Base64.encodeBase64String(ciphertext);
		    	
    	}
    	catch (Exception e) {
    		
    		System.err.println(e.getMessage());
    	}
    	
    	return res;
    }
    
    public static String decrypt(String value) {
    	
    	String plaintext = null;
    	try {
	    	/* Decrypt the message, given derived key and initialization vector. */
    		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");   	
    		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
	    	plaintext = new String(cipher.doFinal(Base64.decodeBase64(value)), "UTF-8");
    	} catch (Exception e) {
    		
    		System.err.println(e.getMessage());
    	}
    	
    	return plaintext;
    }
    
}
