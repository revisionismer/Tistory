package com.tistory.web.api.post;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tistory.config.auth.PrincipalDetails;
import com.tistory.domain.user.User;
import com.tistory.dto.ResponseDto;
import com.tistory.dto.post.PostInfoRespDto;
import com.tistory.dto.post.PostListRespDto;
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
	
	@GetMapping("/{pageOwnerId}")
	public ResponseEntity<?> readAllPost(@PathVariable("pageOwnerId") Long pageOwnerId, @RequestParam(required = false) Long categoryId, @AuthenticationPrincipal PrincipalDetails principalDetails, @PageableDefault(size = 5) Pageable pageable) {

		User loginUser = principalDetails.getUser();
		
		PostListRespDto postListRespDto = postService.readPostList(pageable, loginUser, pageOwnerId, categoryId);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "포스트 리스트 불러오기 성공", postListRespDto), HttpStatus.OK);
	}
	
	@GetMapping("/{pageOwnerId}/{postId}/info")
	public ResponseEntity<?> readbyPostId(@PathVariable("pageOwnerId") Long pageOwnerId, @PathVariable("postId") Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

		User loginUser = principalDetails.getUser();
		
		PostInfoRespDto postInfoRespDto = postService.readPostInfo(pageOwnerId, postId, loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, postId + "번 포스팅 정보 불러오기 성공", postInfoRespDto), HttpStatus.OK);
	}
	
	@PutMapping("/{pageOwnerId}/{postId}/update")
	public ResponseEntity<?> updatePostByPostId(@PathVariable("pageOwnerId") Long pageOwnerId, @PathVariable("postId") Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails, @Valid final PostWriteReqDto postWriteReqDto, BindingResult bindingResult) {
		
		User loginUser = principalDetails.getUser();
		
		PostWriteRespDto postWriteRespDto = postService.updatePost(pageOwnerId, postId, postWriteReqDto, loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, postId + "번 포스팅 정보 수정 성공", postWriteRespDto), HttpStatus.ACCEPTED);
	}
}
