package com.tistory.service.post;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.tistory.domain.category.Category;
import com.tistory.domain.category.CategoryRepository;
import com.tistory.domain.post.Post;
import com.tistory.domain.post.PostRepository;
import com.tistory.domain.user.User;
import com.tistory.domain.visit.Visit;
import com.tistory.domain.visit.VisitRepository;
import com.tistory.dto.post.PostInfoRespDto;
import com.tistory.dto.post.PostListRespDto;
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
	private final VisitRepository visitRepository;
	
	public PostWriteRespDto writePost(PostWriteReqDto postWriteReqDto, User loginUser) {
		String thumnail = null;
		
		String thumnail_ori = null;
		
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
			thumnail_ori = originalFileName;
		}
		
		System.out.println(thumnail);
		
		// 1-11. 카테고리 있는지 확인
		Optional<Category> categoryOp = categoryRepository.findById(postWriteReqDto.getCategoryId());

		// 1-12
		if (categoryOp.isPresent()) {
			Post post = postWriteReqDto.toEntity(thumnail, loginUser, categoryOp.get());
			post.setOriginalImgFileName(thumnail_ori);
			
			Post savedPost = postRepository.save(post);
			
			return new PostWriteRespDto(savedPost);
			
		} else {
			throw new CustomApiException("해당 카테고리가 존재하지 않습니다.");
		}
		
	}
	
	public PostListRespDto readPostList(Long categoryId, Pageable pageable) {
		
		Page<Post> posts = null;
		
		if(categoryId == null) {
			posts = postRepository.findAllPost(pageable);
			
	    } else {
	    	// 2-2. 검색용
	    	posts = postRepository.findAllByCategoryId(categoryId, pageable);
	    }
		
		List<Category> categories = categoryRepository.findAll();
		
		List<Integer> pageNumbers = new ArrayList<>();
		
		for (int i = 0; i < posts.getTotalPages(); i++) {
			pageNumbers.add(i + 1);
	    }
		
		PostListRespDto postListRespDto = new PostListRespDto(
        		posts, 
        		categories, 
        		null, 
        		posts.getNumber() - 1, 
        		posts.getNumber() + 1, 
        		pageNumbers, 
        		null, 
        		(posts.getNumber() - 1) != -1 ? true : false, 
        		(posts.getNumber() + 1) != posts.getTotalPages() ? true : false, 
        		(posts.getContent().size() == 0) ? true : false
        );
        
		return postListRespDto;
		
	}
	
	public PostListRespDto readPostListByPageOwnerId(Pageable pageable, User loginUser, Long pageOwnerId, Long categoryId) {
		// 2-1. 
		Page<Post> posts = null;
		
		if(categoryId == null) {
			posts = postRepository.findByUserId(pageOwnerId, pageable);
					
	    } else {
	    	// 2-2. 검색용
	    	posts = postRepository.findByUserIdAndCategoryId(pageOwnerId, categoryId, pageable);
	    }
	        
		// 2-3. 
		List<Category> categories = categoryRepository.findAll();
		
		// 2-4.
		List<Integer> pageNumbers = new ArrayList<>();
		
		// 2-5. 뷰로 보낼 pageNumber 데이터를 0부터 시작하는게 아니라 1부터 시작하게 +1해준다.
        for (int i = 0; i < posts.getTotalPages(); i++) {
            pageNumbers.add(i + 1);
        }
        
        // 2-6.
        Visit visitEntity = visitIncrease(pageOwnerId, loginUser.getId());
        
        // 2-7.
        PostListRespDto postListRespDto = new PostListRespDto(
        		posts, 
        		categories, 
        		pageOwnerId, 
        		posts.getNumber() - 1, 
        		posts.getNumber() + 1, 
        		pageNumbers, 
        		visitEntity.getTotalCount(), 
        		(posts.getNumber() - 1) != -1 ? true : false, 
        		(posts.getNumber() + 1) != posts.getTotalPages() ? true : false, 
        		(posts.getContent().size() == 0) ? true : false
        );
        
		return postListRespDto;
	}
	
	// 2023-11-02 
	public PostInfoRespDto readPostInfo(Long pageOwnerId, Long postId, User loginUser) {
		
		Optional<Post> postOp = postRepository.findById(postId);
		
		if(postOp.isPresent()) {
			Post findPost = postOp.get();
			
			PostInfoRespDto postInfoRespDto = new PostInfoRespDto(findPost, pageOwnerId == loginUser.getId() ? true : false);
			
			System.out.println(pageOwnerId + ", " + loginUser.getId());
			visitIncrease(pageOwnerId, loginUser.getId());
			
			return postInfoRespDto;
			
		} else {
			throw new CustomApiException(postId + "번 포스팅 정보를 찾을 수 없습니다.");
		}
	}
	
	// 2023-10-26 : 수정은 잘되는데 카테고리가 바뀌었는데 뷰에서는 동적으로 안바뀜
	public PostWriteRespDto updatePost(Long pageOwnerId, Long postId, PostWriteReqDto postWriteReqDto, User loginUser) {
		
		Optional<Post> postOp = postRepository.findById(postId);
		
		if(postOp.isPresent()) {
			Post findPost = postOp.get();
			
			if(loginUser.getId().equals(pageOwnerId)) {
				
				String thumnail = null;
				
				String thumnail_ori = null;
				
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
					thumnail_ori = originalFileName;
				}
				
				System.out.println(thumnail);
				
				// 1-11. 카테고리 있는지 확인
				Optional<Category> categoryOp = categoryRepository.findById(postWriteReqDto.getCategoryId());

				// 1-12
				if (categoryOp.isPresent()) {
					Category findCategory = categoryOp.get();
					
					findPost.setTitle(postWriteReqDto.getTitle());
					findPost.setContent(postWriteReqDto.getContent());
					findPost.setCategory(findCategory);
					
					if(thumnail != null && thumnail_ori != null) {
						findPost.setThumnailImgFileName(thumnail);
						findPost.setOriginalImgFileName(thumnail_ori);
					}
					
					findPost.setUser(loginUser);
					findPost.setUpdatedAt(LocalDateTime.now());
					
					Post updatedPost = postRepository.save(findPost);
					
					return new PostWriteRespDto(updatedPost);
					
				} else {
					throw new CustomApiException("해당 카테고리가 존재하지 않습니다.");
				}
				
				
			} else {
				throw new CustomApiException("수정할 권한이 없습니다.");
			}
			
			
		} else {
			throw new CustomApiException(postId + "번 포스팅 정보를 찾을 수 없습니다.");
		}
		
	}
	
	private Visit visitIncrease(Long pageOwnerId, Long principalId) {	
		// 3-1. 회원가입시 만들어진 방문 엔티티를 페이지 주인의 id로 찾아온다.
        Optional<Visit> visitOp = visitRepository.findById(pageOwnerId);
        
        // 3-2. 페이지 주인의 방문엔티티가 존재한다면
        if (visitOp.isPresent()) {
        	// 3-3. 엔티티를 가져와서
            Visit visitEntity = visitOp.get();
            
            // 3-4. 전체 방문수를 가져온다.
            Long totalCount = visitEntity.getTotalCount();
            
            // 3-5. 페이지주인의 아이디와 로그인한 회원의 아이디가 다를때만
            if(pageOwnerId != principalId) {
            	// 3-6. 방문자 수를 1씩 증가시켜준다.
                visitEntity.setTotalCount(totalCount + 1L);
            }
            
            return visitEntity;
            
        } else {
            throw new CustomApiException("일시적 문제가 생겼습니다. 관리자에게 문의해주세요.");
        }
	}
}
