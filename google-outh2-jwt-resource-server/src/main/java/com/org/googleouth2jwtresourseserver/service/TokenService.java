package com.org.googleouth2jwtresourseserver.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.org.googleouth2jwtresourseserver.configuration.OAuthProperties;
import com.org.googleouth2jwtresourseserver.google.GoogleChecker;
import com.org.googleouth2jwtresourseserver.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Autowired
	private OAuthProperties oAuthProperties;
	
	@Autowired 
	private UserRepository userRepository;
	

	public boolean isAuthenticatedToken() {
		//TODO implement method
		return false;
	}
	
	public UsernamePasswordAuthenticationToken verifyToken(String token) {
		String CLIENT_ID = oAuthProperties.getClientId();        
        GoogleChecker googleChecker = new GoogleChecker(new String[]{CLIENT_ID}, CLIENT_ID);
        
        GoogleIdToken.Payload jwtObject = googleChecker.check(token); 
        if(jwtObject != null) { //jwtObject.get("given_name")    {"at_hash":"Z6a2kNmRPCSZlSWMI2brag","aud":"252882737828-4nijfj919fdsokaalhgcada9djg0ttth.apps.googleusercontent.com","azp":"252882737828-4nijfj919fdsokaalhgcada9djg0ttth.apps.googleusercontent.com","email":"igutierrez@mobydigital.com","email_verified":true,"exp":1594589992,"hd":"mobydigital.com","iat":1594586392,"iss":"https://accounts.google.com","nonce":"0YMEgRFNfrN92O9fpNdQdHkkEmwezz0Pctg_ugrY_Rw","sub":"118075249841973988243","name":"Iván Gutierrez","picture":"https://lh3.googleusercontent.com/a-/AOh14Gi2WUj784hiw9K1yXBtzKBU_rPDYZkrLymdyXUA=s96-c","given_name":"Iván","family_name":"Gutierrez ","locale":"es"}
        	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        			jwtObject.getEmail(), null, new ArrayList<>());
        	return usernamePasswordAuthenticationToken;
        }
        return null;
	}
	
	public String generateAppToken(String username) {
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						oAuthProperties.getSecretKey().getBytes()).compact();

		return "Bearer " + token;
	}
}
