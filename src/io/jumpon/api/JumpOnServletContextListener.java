package io.jumpon.api;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import io.jumpon.api.db.LastUser;
import io.jumpon.api.library.*;
import io.jumpon.api.library.encryption.Encryption;

public class JumpOnServletContextListener 
               implements ServletContextListener{
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		Log.debug("ServletContextListener destroyed");
	}

        //Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		
		//Load context parameters (Global)
		Globals.loadContextData(arg0.getServletContext());
		
		Log.info("LogLevel:"+Globals.getLogLevel());
		
		Log.debug("ServletContextListener started");	
		Log.debug("**");
		Log.debug("Catalina base path:"+System.getProperty("catalina.base"));
		Log.debug("Relative path:"+ arg0.getServletContext().getRealPath("/credentials"));		
		Log.debug("**");		
		
		
		//Establish DB connection
		DBConnection.init();

		
		Encryption.init(arg0.getServletContext().getInitParameter("jumponPrivateKeyFile"),
						arg0.getServletContext().getInitParameter("jumponPublicKeyFile"));		
		
		Log.debug("");		
		Log.debug("Max USER ID:"+LastUser.getLastUserID().toString());
		Log.debug("");		
		
		UserSequencer.setInitialValue(LastUser.getLastUserID()+1);
		//System.out.println("NEXT USER ID:"+String.valueOf(UserSequencer.next()));
		Log.debug("**************");
		
		Log.debug("LOAD COMPLETE");
		Log.debug("**************");			
	}
}