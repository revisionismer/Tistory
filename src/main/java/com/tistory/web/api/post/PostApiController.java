package com.tistory.web.api.post;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tistory.config.auth.PrincipalDetails;
import com.tistory.domain.user.User;
import com.tistory.dto.ResponseDto;
import com.tistory.dto.post.PostWriteReqDto;
import com.tistory.dto.post.PostWriteRespDto;
import com.tistory.service.post.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostApiController {
	
	private final PostService postService;
	
	@PostMapping("/write")
	public ResponseEntity<?> writePost(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid final PostWriteReqDto postWriteReqDto, BindingResult bindingResult) {

		User loginUser = principalDetails.getUser();
		
		PostWriteRespDto postWriteRespDto = postService.writePost(postWriteReqDto, loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "포스트 글쓰기 성공", postWriteRespDto), HttpStatus.CREATED);
	}
}
