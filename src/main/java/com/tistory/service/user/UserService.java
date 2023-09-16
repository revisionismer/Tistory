package com.tistory.service.user;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tistory.domain.user.User;
import com.tistory.domain.user.UserRepository;
import com.tistory.dto.join.JoinReqDto;
import com.tistory.dto.join.JoinRespDto;
import com.tistory.dto.user.UserRespDto.UserInfoRespDto;
import com.tistory.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final UserRepository userRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;
	
	public JoinRespDto join(JoinReqDto joinReqDto) {
		log.info("회원가입");
		// 1-1. 동일 회원 아이디가 있는지 확인
		Optional<User> userOp = userRepository.findByUsername(joinReqDto.getUsername());
		
		if(userOp.isPresent()) {
			throw new CustomApiException("중복된 아이디입니다.");
		}
		
		// 1-2. 패스워드 인코딩 + 회원가입
		User userPS = userRepository.save(joinReqDto.toEntity(passwordEncoder));
		
		// 1-3. dto 응답
		return new JoinRespDto(userPS);
	}
	
	@Transactional(readOnly = true)
	public UserInfoRespDto userInfo(User loginUser) {
		log.info("로그인 유저 정보 DB에서 가져오기");
		// 1-1. 전달 받은 User entity 정보가 있는지 확인
		Optional<User> userOp = userRepository.findByUsername(loginUser.getUsername());
				
		if(userOp.isPresent()) {
			User findUser = userOp.get();
					
			return new UserInfoRespDto(findUser);
					
		} else {
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}
		
	}
}
