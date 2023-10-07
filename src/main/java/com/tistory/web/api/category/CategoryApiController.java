package com.tistory.web.api.category;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tistory.config.auth.PrincipalDetails;
import com.tistory.domain.category.Category;
import com.tistory.domain.user.User;
import com.tistory.dto.ResponseDto;
import com.tistory.dto.category.CategoryWriteReqDto;
import com.tistory.dto.category.CategoryWriteRespDto;
import com.tistory.service.category.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryApiController {
	
	private final CategoryService categoryService;

	@PostMapping("/write") 
	public ResponseEntity<?> createCategory(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody @Valid CategoryWriteReqDto categoryWriteReqDto, BindingResult bindingResult) {
		
		User loginUser = principalDetails.getUser();
		
		Category category = categoryWriteReqDto.toEntity(loginUser);
		
		CategoryWriteRespDto categoryWriteRespDto = categoryService.categoryRegister(category, loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "카테고리 만들기 성공", categoryWriteRespDto), HttpStatus.CREATED);
	}
}
