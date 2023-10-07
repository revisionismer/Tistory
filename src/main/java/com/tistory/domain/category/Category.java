package com.tistory.domain.category;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.tistory.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter @Setter
@Table(name = "category_tb")
@Entity
public class Category {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // UTF-8 (가변 인코딩 : 영어 1Byte, 한글 3Byte)
    @Column(length = 60, nullable = false)
    private String title;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;
    
    @CreatedDate
 	@Column(nullable = false)
 	private LocalDateTime createdAt;
 	 
 	@LastModifiedDate
 	@Column(nullable = true)
 	private LocalDateTime updatedAt; 
 	
 	@Builder
    public Category(Long id, String title, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
 	
 	
}
