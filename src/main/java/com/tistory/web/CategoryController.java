package com.tistory.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/category")
public class CategoryController {
	
	@GetMapping("/write")
	public String categoryWriteForm() {
		return "category/categoryForm";
	}

}
