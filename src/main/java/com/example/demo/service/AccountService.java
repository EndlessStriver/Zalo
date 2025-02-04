package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entities.Account;

public interface AccountService {

	Account findById(String accountId);
	
	Account findByUsername(String username);
	
	Account findByEmail(String email);

	List<Account> findAll();
	
	Account login(LoginRequest loginRequest);
	
	Account register(RegisterRequest registerRequest);
	
	boolean isAccountVerifiedByEmail(boolean verify, String email);
	
	void verifyByEmail(String email);

}
