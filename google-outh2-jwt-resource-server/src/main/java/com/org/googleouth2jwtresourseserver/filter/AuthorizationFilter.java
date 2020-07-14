package com.org.googleouth2jwtresourseserver.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.org.googleouth2jwtresourseserver.service.TokenService;

@Component
public class AuthorizationFilter extends OncePerRequestFilter
{
	@Autowired
	private TokenService tokenService;

	@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
		
		if(tokenService.isAppTokenRequest(request)) {
			chain.doFilter(request, response);		
		} else {
			final String token = tokenService.getTokenFromRequest(request);
			if(token != null) {
				Authentication user = tokenService.verifyAppToken(token);
				SecurityContextHolder.getContext().setAuthentication(user);
				chain.doFilter(request, response);			
			} else {
				response.sendError(HttpStatus.SC_FORBIDDEN);
			}
		}
		
	}
      

}
