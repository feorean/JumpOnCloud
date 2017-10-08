package io.jumpon.api.library.encryption;



import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import org.apache.commons.codec.binary.Base64;

public class Encryption {
	
  	static private PrivateKey privateKey;
  	static private PublicKey publicKey;
	static private KeyPair keyPair;



  	static private void setPrivateKey(String file) {
  		
  		try {
  			privateKey = PrivateKeyReader.get(file);
  		
  		} catch (Exception e) {
  			
  			System.err.println("Can not read private key:"+e.getMessage());
  		}
  	}

  	static private void setPublicKey(String file) {
  		
  		try {
  			publicKey = PublicKeyReader.get(file);
  		
  		} catch (Exception e) {
  			
  			System.err.println("Can not read public key:"+e.getMessage());
  		}
  	}  	
  	
  	static public void init(String privateKeyFile, String publicKeyFile) {
  		
  		setPrivateKey(privateKeyFile);
  		setPublicKey(publicKeyFile);
  		
	    keyPair = new KeyPair(publicKey, privateKey);
	    


  	}
  	
  	static public String encryptText(String text) {
  		
  		String res = null;
  		
  		try {
  			
  			byte[] data = text.getBytes("UTF8");

			Signature sig = null;
		
			sig = Signature.getInstance("MD5WithRSA");
			//Load private key
		    sig.initSign(keyPair.getPrivate());
		    
		    //Add data
		    sig.update(data);
    	    
		    //Sign
		    byte[] signatureBytes = sig.sign();

		    res = Base64.encodeBase64String(signatureBytes);
	    
		    //Print signature
		    //System.out.println("Singature:" + res);
	  		
	    } catch (Exception e) {
	    	System.err.println("Can not create signature:"+e.getMessage());
	    }		    
	    
	    return res;
  		
  	}
  	
  	static public boolean verifySignature(String text, String signature) {

  		boolean res = false;
  		byte[] data = null;
  		byte[] signatureBytes = null;  		
		try {
			data = text.getBytes("UTF8");
			signatureBytes = Base64.decodeBase64(signature);
		
			Signature sig = Signature.getInstance("MD5WithRSA");	    
	  		
		    //Load Public key for verification
		    sig.initVerify(keyPair.getPublic());
	
		    //Add data again
		    sig.update(data);
	
		    //System.out.println(sig.verify(signatureBytes));
  		
		    res = sig.verify(signatureBytes);
		} catch (Exception e) {
			System.err.println("Error in verification:"+e.getMessage());
		}	    
	    
  		return res;
  	}
  	
  	
}
