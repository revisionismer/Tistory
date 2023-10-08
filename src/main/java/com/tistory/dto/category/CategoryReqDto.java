package com.tistory.dto.category;

import com.tistory.domain.category.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class CategoryReqDto {
	
	private Long id;
	private String title;
	private Long userId;
	
	public CategoryReqDto(Category categoryEntity) {
		this.id = categoryEntity.getId();
		this.title = categoryEntity.getTitle();
		this.userId = categoryEntity.getUser().getId();
	}
}
