package com.tistory.service.post;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.tistory.domain.category.Category;
import com.tistory.domain.category.CategoryRepository;
import com.tistory.domain.post.Post;
import com.tistory.domain.post.PostRepository;
import com.tistory.domain.user.User;
import com.tistory.dto.post.PostWriteReqDto;
import com.tistory.dto.post.PostWriteRespDto;
import com.tistory.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

	@Value("${thumnail.path}")
    private String thumnailFolder;
	
	private final PostRepository postRepository;
	private final CategoryRepository categoryRepository;
	
	public PostWriteRespDto writePost(PostWriteReqDto postWriteReqDto, User loginUser) {
		String thumnail = null;
		
		// 1-1. 썸네일 이미지 파일이 nullㅣ이거나 비어있지 않으면 진행
		if (!(postWriteReqDto.getThumnailFile() == null || postWriteReqDto.getThumnailFile().isEmpty())) {

			// 1-2. originalFileName을 가져온다.
			String originalFileName = postWriteReqDto.getThumnailFile().getOriginalFilename();
			System.out.println(originalFileName);
			
			// 1-4. 확장자 추출
			String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
			System.out.println(extension);
			
			// 1-5. UUID[Universally Unique IDentifier] : 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약. 범용고유식별자.
			UUID uuid = UUID.randomUUID();
						
			// 1-6. UUID + 확장자 명으로 저장
			String thumnailFileName = uuid.toString() + extension;			
			System.out.println(thumnailFileName);
			
			// 1-7. 실제 파일이  저장될 경로 + 파일명 저장.
			Path thumnailFilePath = Paths.get(thumnailFolder + thumnailFileName);
			System.out.println(thumnailFilePath);

			// 1-8. 파일을 실제 저장 경로에 저장하는 로직 -> 통신 or I/O -> 예외가 발생할 수 있다(try-catch로 묶어서 처리)
			try {
				Files.write(thumnailFilePath, postWriteReqDto.getThumnailFile().getBytes());
			} catch (Exception e) {
				// 1-9. 설정된 MB를 넘어가면 -1을 반환
				throw new MaxUploadSizeExceededException(600);
			}
			
			// 1-10. 썸네일 파일 이름 저장.
			thumnail = thumnailFileName;
		}
		
		System.out.println(thumnail);
		
		// 1-11. 카테고리 있는지 확인
		Optional<Category> categoryOp = categoryRepository.findById(postWriteReqDto.getCategoryId());

		// 1-12
		if (categoryOp.isPresent()) {
			Post post = postWriteReqDto.toEntity(thumnail, loginUser, categoryOp.get());
			
			Post savedPost = postRepository.save(post);
			
			return new PostWriteRespDto(savedPost);
			
		} else {
			throw new CustomApiException("해당 카테고리가 존재하지 않습니다.");
		}
		
	}
}
