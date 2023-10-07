package com.tistory.service.category;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tistory.constant.user.UserEnum;
import com.tistory.domain.category.Category;
import com.tistory.domain.category.CategoryRepository;
import com.tistory.domain.user.User;
import com.tistory.dto.category.CategoryWriteRespDto;
import com.tistory.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	
	public CategoryWriteRespDto categoryRegister(Category category, User loginUser) {
		
		if(loginUser.getRole() == UserEnum.ADMIN) {
			category.setCreatedAt(LocalDateTime.now());
			
			Category newCategory = categoryRepository.save(category);
		
			return new CategoryWriteRespDto(newCategory);
		} else {
			throw new CustomApiException("관리자로 로그인 해주세요.");
		}
	
	}

	@Transactional(readOnly = true)
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}
}