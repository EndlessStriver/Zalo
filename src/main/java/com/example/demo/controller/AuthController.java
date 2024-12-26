package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.ResponseSuccess;
import com.example.demo.entities.Account;
import com.example.demo.service.AccountService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private AccountService accountService;
	
	public AuthController(AccountService accountService) {
		this.accountService = accountService;
	}	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new ResponseSuccess<Account>(HttpStatus.OK.value(), "Đăng nhập thành công", accountService.login(loginRequest)));
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseSuccess<Account>(HttpStatus.OK.value(), "Đăng ký thành công", accountService.register(registerRequest)));
	}
	
}
