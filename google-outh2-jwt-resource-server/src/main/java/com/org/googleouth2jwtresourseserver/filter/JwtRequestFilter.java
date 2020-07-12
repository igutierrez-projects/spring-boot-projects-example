package com.org.googleouth2jwtresourseserver.filter;


import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
	
	private final String JWT_HEADER = "Bearer ";
	private final String AUTHORIZATION_HEADER = "Authorization";

    //@Autowired
    //private JwtUtil jwtUtil;
    
	@Autowired
	private OAuthProperties oAuthProperties;
	
	@Autowired
	private TokenService tokenService;
  
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
    	
    	final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        
    	if( !request.getMethod().equals(HttpMethod.POST.name()) || authorizationHeader == null || (authorizationHeader != null && !authorizationHeader.startsWith("Bearer ")) ) {
    		response.sendError(HttpStatus.SC_BAD_REQUEST);
    		return;
    	} 
    	
    	String token = authorizationHeader.substring(7);   
    	UsernamePasswordAuthenticationToken user = tokenService.verifyToken(token);
    	if( user != null) {
    		user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        	SecurityContextHolder.getContext().setAuthentication(user);   
            //chain.doFilter(request, response);
        	response.addHeader(AUTHORIZATION_HEADER, tokenService.generateAppToken(user.getPrincipal().toString()));
    	} else {
    		response.sendError(HttpStatus.SC_FORBIDDEN);
    	}
    	
     }

}
