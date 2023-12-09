package com.tistory.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	// 2023-12-04 : 여기까지 연결
	@PostMapping("/board/detail")
	public String boardDetail(@RequestParam("principalId") Long principalId, @RequestParam("boardId") Long boardId, Model model) {
		
		model.addAttribute("principalId", principalId);
		model.addAttribute("boardId", boardId);
		
		return "board/boardDetail";
	}
	
}
