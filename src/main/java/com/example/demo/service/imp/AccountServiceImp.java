package com.example.demo.service.imp;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entities.Account;
import com.example.demo.entities.User;
import com.example.demo.entities.enums.RoleAccount;
import com.example.demo.exception.AuthenticationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AccountService;

@Service
public class AccountServiceImp implements AccountService {
	
	private AccountRepository accountRepository;
	private AuthenticationManager authenticationManager;
	
	public AccountServiceImp(AccountRepository accountRepository, AuthenticationManager authenticationManager) {
		this.accountRepository = accountRepository;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Account getAccountById(String accountId) {
		return accountRepository.findById(accountId).orElse(null);
	}

	@Override
	public Account getAccountByUsername(String username) {
		return accountRepository.getAccountByUsername(username).orElse(null);
	}

	@Override
	public List<Account> getAllAccount() {
		return accountRepository.findAll();
	}

	@Override
	public Account login(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			Account account = accountRepository.getAccountByUsername(loginRequest.getUsername()).orElse(null);
			if(account == null) throw new ResourceNotFoundException("Không tìm thấy tài khoản với tên đăng nhập là: " + loginRequest.getUsername());
			return account;
		} else {
			throw new AuthenticationException("Tài khoản hoặc mật khẩu không chính xác!");
		}
	}

	@Override
	public Account register(RegisterRequest registerRequest) {
		Account account = new Account();
		account.setUsername(registerRequest.getUsername());
		account.setPassword(registerRequest.getPassword());
		account.setRole(RoleAccount.USER);
		
		User user = new User();
		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setGender(registerRequest.getGender());
		user.setBirthday(registerRequest.getBirthday());
		user.setEmail(registerRequest.getEmail());
		user.setAccount(account);
		
		account.setUser(user);
		
		return accountRepository.save(account);
	}
}
