package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entities.Account;

public interface AccountService {

	Account getAccountById(String accountId);
	
	Account getAccountByUsername(String username);

	List<Account> getAllAccount();
	
	Account login(LoginRequest loginRequest);
	
	Account register(RegisterRequest registerRequest);

}
