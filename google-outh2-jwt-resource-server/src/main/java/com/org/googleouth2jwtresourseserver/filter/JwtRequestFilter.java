package com.org.googleouth2jwtresourseserver.filter;


import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.org.googleouth2jwtresourseserver.configuration.OAuthProperties;
import com.org.googleouth2jwtresourseserver.google.GoogleChecker;
import com.org.googleouth2jwtresourseserver.service.TokenService;
import com.org.googleouth2jwtresourseserver.utils.JwtUtil;



import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenService tokenService;
  
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
    	
    	final String token = tokenService.getTokenFromRequest(request);
    	
    	if( this.isPostMethod(request) &&  token != null ) {    		
    		try {    			
    			Authentication user = this.loadAuthenticationContext(token, request);            	
            	response.addHeader(HttpHeaders.AUTHORIZATION, tokenService.generateAppToken(user.getPrincipal().toString()));    			
    		}catch (Exception e) {
    			response.sendError(HttpStatus.SC_FORBIDDEN);
			}
    			
    	} else {
    		response.sendError(HttpStatus.SC_BAD_REQUEST);  
    	}
    }  	
    
    private boolean isPostMethod(HttpServletRequest request) {
    	return request.getMethod().equals(HttpMethod.POST.name());
    }
    
    private Authentication loadAuthenticationContext(String token, HttpServletRequest request) throws Exception{
    	UsernamePasswordAuthenticationToken user = tokenService.verifyTokenFromGoogle(token);
		if( user != null) {
    		user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //Optional
        	SecurityContextHolder.getContext().setAuthentication(user);
        	return user;
		} else {
			throw new Exception();
		}
    }
		

}
