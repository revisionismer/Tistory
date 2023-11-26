package com.tistory.dto.board;

import java.time.LocalDateTime;

import com.tistory.domain.board.Board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRespDto {
	
	private Long id; // 1. PK
	
	private String title; // 2. 제목
	
	private String content; // 3. 내용
	
	private String writer; // 4. 작성자
	
	private int hits;  // 5. 조회수
	
	private char deleteYn; // 6. 삭제여부
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	public BoardRespDto(Board entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.writer = entity.getWriter();
		this.hits = entity.getHits();
		this.deleteYn = entity.getDeleteYn();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();
	}
}