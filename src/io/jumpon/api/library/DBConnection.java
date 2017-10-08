package io.jumpon.api.library;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class DBConnection {

	//Global for Dbapi instances
	public static AmazonDynamoDBClient dynamoDBClient = null;
	public static DynamoDB dynamoDB = null;
	private static String aws_credentials_location = "default";
    
    
    //Automatically create a session
    
    public static void init() {
    	
    	
    	aws_credentials_location = Globals.getAWSCredentials();
    	
    	
    	Log.info("AWS cred. localation"+aws_credentials_location);
    	
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (/Users/Khalid/.aws/credentials).
         */
        AWSCredentials credentials = null;
        
        if (dynamoDBClient == null) {
        	
	        try {
	        	
	        	ProfilesConfigFile cfg = new ProfilesConfigFile(aws_credentials_location);
	        	 
	        	credentials = new ProfileCredentialsProvider(cfg, "default").getCredentials();
	        	
	            //credentials = new ProfileCredentialsProvider(aws_credentials_location).getCredentials();
	        } catch (Exception e) {
	            throw new AmazonClientException(
	                    "Cannot load the credentials from the credential profiles file. " +
	                    "Please make sure that your credentials file is at the correct " +
	                    "location (/Users/Khalid/.aws/credentials), and is in valid format.",
	                    e);
	        }
	        dynamoDBClient = new AmazonDynamoDBClient(credentials);
	        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	        dynamoDBClient.setRegion(usWest2);
	        dynamoDB = new DynamoDB(dynamoDBClient);
	        Log.info("AWS Client setup complete.");
        }    	
        
        //loadUserBuckets();
    }	
	
}
