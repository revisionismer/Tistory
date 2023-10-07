package com.tistory.service.post;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tistory.domain.user.User;
import com.tistory.dto.post.PostWriteReqDto;
import com.tistory.dto.post.PostWriteRespDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	@Value("${thumnail.path}")
    private String thumnailFolder;
	
	public PostWriteRespDto writePost(PostWriteReqDto postWriteReqDto, User loginUser) {
		
		return null;
		
	}
}
