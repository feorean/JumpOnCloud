package io.jumpon.api.library.session;

public class SimpleSession implements Session {

	private Object sessionInfo;
	
	@Override
	public Object getSessionInfo() {
		
		return sessionInfo;
		
	}

	@Override
	public void setSessionInfo(Object sess) {
		
		sessionInfo = sess;
		
	}

	public SimpleSession(Object sess) {
		
		setSessionInfo(sess);
	}
	
}
