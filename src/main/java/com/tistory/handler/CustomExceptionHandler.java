package com.tistory.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tistory.dto.ResponseDto;
import com.tistory.handler.exception.CustomApiException;
import com.tistory.handler.exception.CustomValidationException;

@RestControllerAdvice // 1-1. @ControllerAdvice + @RestController : 모든 exception을 낚아챈다.
public class CustomExceptionHandler {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@ExceptionHandler(CustomApiException.class)  // 1-2. CustomApiException이 터지면 여기서 캐치해서 매개변수로 넘겨준다.
	public ResponseEntity<?> apiException(CustomApiException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomValidationException.class)  // 2-1. CustomValidationException
	public ResponseEntity<?> validationException(CustomValidationException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
	}
}
