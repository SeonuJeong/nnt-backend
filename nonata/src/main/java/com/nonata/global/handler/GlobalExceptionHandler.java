package com.nonata.global.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.nonata.domain.dto.ResponseDto;

@ControllerAdvice // exception 발생하면 여기로 옴
@RestController
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = Exception.class)
	public ResponseDto<String> handleArgumentException(Exception e) {
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
	}
}
