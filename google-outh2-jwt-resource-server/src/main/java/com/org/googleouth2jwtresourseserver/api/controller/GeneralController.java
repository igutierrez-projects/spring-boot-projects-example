package com.org.googleouth2jwtresourseserver.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GeneralController {

	@GetMapping("/welcome")
	public String success() {
		return "Welcome";
	}
	
}
