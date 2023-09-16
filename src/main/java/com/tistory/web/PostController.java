package com.tistory.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
public class PostController {

	@GetMapping("/list")
	public String postList() {
		return "post/postList";
	}
	
	@GetMapping("/write")
	public String postWrite() {
		return "post/postForm";
	}
	
}
