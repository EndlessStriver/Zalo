package com.example.demo.unit;


import org.springframework.stereotype.Component;

import com.example.demo.entities.Account;
import com.example.demo.service.AccountService;
import com.example.demo.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class MethodUnit {
	
	private JwtService jwtService;
	private AccountService accountService;
	
	public MethodUnit(JwtService jwtService, AccountService accountService) {
		this.jwtService = jwtService;
		this.accountService = accountService;
	}
	
	public Account getAccountFromToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		String jwt = authorizationHeader.substring(7);
		String usernameSender = jwtService.extractUsername(jwt);
		return accountService.getAccountByUsername(usernameSender);
	}
}
