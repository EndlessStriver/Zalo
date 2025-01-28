package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.dto.ResponseError;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception ex) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ResponseError(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ResponseError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
	}

	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<?> handleAuthorizationException(AuthorizationException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new ResponseError(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<?> handleConflictException(ConflictException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ResponseError(HttpStatus.CONFLICT.value(), ex.getMessage()));
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> handleConflictException(BadRequestException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<?> handleConflictException(ExpiredJwtException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Token đã hết hạn"));
	}

	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<?> handleConflictException(SignatureException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Token không hợp lệ"));
	}

	@ExceptionHandler(InternalAuthenticationServiceException.class)
	public ResponseEntity<?> handleConflictException(InternalAuthenticationServiceException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingRequestParam(MissingServletRequestParameterException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ResponseError(HttpStatus.BAD_REQUEST.value(), "Thiếu tham số bắt buộc: " + ex.getParameterName()));
	}
}
