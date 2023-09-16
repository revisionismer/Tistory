package com.tistory.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping({"/", "/index"})
	public String index() {
		return "index";
	}
	
	@GetMapping("/join")
	public String joinForm() {
		return "joinForm";
	}
	
	@GetMapping("/login")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/password-reset-form")
	public String passwordResetForm() {
		return "passwordResetForm";
	}
}
