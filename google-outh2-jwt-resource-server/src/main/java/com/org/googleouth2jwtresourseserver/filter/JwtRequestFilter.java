package com.org.googleouth2jwtresourseserver.filter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.org.googleouth2jwtresourseserver.configuration.OAuthProperties;
import com.org.googleouth2jwtresourseserver.google.GoogleChecker;
import com.org.googleouth2jwtresourseserver.utils.JwtUtil;



import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    //@Autowired
    //private JwtUtil jwtUtil;
    
	@Autowired
	private OAuthProperties oAuthProperties;
    //@Autowired
    //private GoogleChecker googleChecker;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
    	
        final String authorizationHeader = request.getHeader("Authorization");
        
        String CLIENT_ID = oAuthProperties.getClientId();        
        GoogleChecker googleChecker = new GoogleChecker(new String[]{CLIENT_ID}, CLIENT_ID);
       

        String username = "username@google";
        GoogleIdToken.Payload jwt_object = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {            
            String idToken = authorizationHeader.substring(7);;        
            jwt_object = googleChecker.check(idToken); 
            
        }
        if(jwt_object != null) {
        	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null, new ArrayList<>());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }       
      
        chain.doFilter(request, response);
      	}

}
