package com.tistory.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class BoardController {
	
	@GetMapping("/board/list")
	public String boardList() {
		return "board/boardList";
	}
	
	@GetMapping("/board/write")
	public String boardWriteForm() {
		return "board/boardWriteForm";
	}
	
}
