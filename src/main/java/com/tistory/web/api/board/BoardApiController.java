package com.tistory.web.api.board;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tistory.config.auth.PrincipalDetails;
import com.tistory.domain.user.User;
import com.tistory.dto.ResponseDto;
import com.tistory.dto.board.BoardPagingRespDto;
import com.tistory.dto.board.BoardReqDto;
import com.tistory.dto.board.BoardRespDto;
import com.tistory.dto.board.paging.CommonParams;
import com.tistory.service.board.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardApiController {
	
	private final BoardService boardService;
	
	@PostMapping("/s")
	public ResponseEntity<?> writeBoard(@RequestBody @Valid final BoardReqDto boardRequestDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) { 
		
		User loginUser = principalDetails.getUser();
		
		BoardRespDto boardRespDto = boardService.writeBoard(boardRequestDto, loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "포스팅 글쓰기 성공", boardRespDto), HttpStatus.OK);
	}
	
//	@GetMapping("")
	public ResponseEntity<?> selectBoardByAll(@AuthenticationPrincipal PrincipalDetails principalDetails) { 
		
		List<BoardRespDto> result = boardService.findAll();
		
		return new ResponseEntity<>(new ResponseDto<>(1, "포스팅 글 리스트 불러오기 성공", result), HttpStatus.OK);
	}
	
	@GetMapping("")  // 2023-11-26 : 1페이지부터 게시글 페이징 데이터 불러오는거 성공.
	public ResponseEntity<?> selectBoardByPaging(final CommonParams params, @AuthenticationPrincipal PrincipalDetails principalDetails) { 
		
		BoardPagingRespDto boardPagingRespDto = boardService.findAllByPaging(params);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "포스팅 글 리스트 불러오기 성공", boardPagingRespDto), HttpStatus.OK);
	}
	
	@PatchMapping("/{boardId}")
	public ResponseEntity<?> updateBoard(@PathVariable Long boardId, @RequestBody @Valid final BoardReqDto boardRequestDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) { 
		
		User loginUser = principalDetails.getUser();
		
		boardService.update(boardId, boardRequestDto, loginUser);
		
		return new ResponseEntity<>(new ResponseDto<>(1, "포스팅 글쓰기 수정 성공", null), HttpStatus.OK);
	}
}
