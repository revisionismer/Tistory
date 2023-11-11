package com.tistory.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/post")
public class PostController {

	@GetMapping("/list")
	public String postList() {
		return "post/postList";
	}
	
	@GetMapping("/write")
	public String postWriteForm() {
		return "post/postForm";
	}
	
	@PostMapping("/detail")
	public String postDetail(@RequestParam("principalId") Long principalId, @RequestParam("postId") Long postId, Model model) {
		
		System.out.println("principalId : " + principalId + ", postId = " + postId);
		
		model.addAttribute("principalId", principalId);
		model.addAttribute("postId", postId);
		
		return "post/postForm";
	}
	
	@PostMapping("/view")
	public String postView(@RequestParam("pageOwnerId") Long pageOwnerId, @RequestParam("principalId") Long principalId, @RequestParam("postId") Long postId, Model model) {
		
		System.out.println("pageOwnerId : " + pageOwnerId + ", principalId : " + principalId + ", postId = " + postId);
		
		model.addAttribute("pageOwnerId", pageOwnerId);
		model.addAttribute("principalId", principalId);
		model.addAttribute("postId", postId);
		
		return "post/postView";
	}
	
}
