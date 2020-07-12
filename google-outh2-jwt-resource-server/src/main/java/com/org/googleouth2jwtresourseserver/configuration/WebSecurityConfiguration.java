package com.org.googleouth2jwtresourseserver.configuration;

import java.util.Arrays;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.org.googleouth2jwtresourseserver.filter.JwtRequestFilter;


@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
		
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	@Autowired 
	private OAuthProperties oAuthProperties;
	

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
		.authorizeRequests().antMatchers(oAuthProperties.getAppTokenURI()).permitAll().
				anyRequest().authenticated().and().
				exceptionHandling().and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
	}
	
	@Bean
	public FilterRegistrationBean<Filter> appTokenFilter() {
		FilterRegistrationBean<Filter> frb = new FilterRegistrationBean<>();
		frb.setFilter(jwtRequestFilter);
		frb.setUrlPatterns(Arrays.asList(oAuthProperties.getAppTokenURI()));
		
		return frb;
				
	}
}
