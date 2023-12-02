package com.tistory.domain.board;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter @Setter
@Table(name = "boardFile_tb")
@Entity
public class BoardFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fileId;
	
	private String fileName;
	
	private String fileUrl;
	
	private Long userId;
	
	private Long boardId;
	
	private Integer downCnt;
	
	@CreatedDate
 	@Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	private LocalDateTime createdAt; 
 	 
 	@LastModifiedDate
 	@Column(nullable = true)
 	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
 	private LocalDateTime updatedAt;  
}
