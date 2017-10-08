package io.jumpon.api;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

/**

 */
public class RequestFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain)
    throws IOException, ServletException {

    	
    	
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        System.err.println(req.getRequestURL() +" "+ req.getRequestURI());
        
    	res.addHeader(
    	        "Cache-Control",
    	        "no-cache, max-age=0, must-revalidate, no-store");        
        
        filterChain.doFilter(req, res);
        
    	//res.addHeader(
    	//        "Cache-Control",
    	//        "no-cache, max-age=0, must-revalidate, no-store");
    	
    	/*Iterator<String> headerNames = res.getHeaderNames().iterator();
        while (headerNames.hasNext()) {
            String headerName = (String) headerNames.next();
            System.err.println(headerName + "::" + res.getHeader(headerName));
        }*/
    	
    	System.err.println("Filtering..");
    }

    public void destroy() {
    }
}