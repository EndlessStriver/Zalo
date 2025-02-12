package com.example.demo.service.imp;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entities.Account;
import com.example.demo.entities.User;
import com.example.demo.entities.enums.Gender;
import com.example.demo.entities.enums.RoleAccount;
import com.example.demo.exception.AuthenticationException;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.UserService;

@Service
public class AccountServiceImp implements AccountService {
	
	private AccountRepository accountRepository;
	private UserService userService;
	private AuthenticationManager authenticationManager;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public AccountServiceImp(AccountRepository accountRepository, UserService userService,
			AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.accountRepository = accountRepository;
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public Account findById(String accountId) {
		return accountRepository.findById(accountId).orElse(null);
	}

	@Override
	public Account findByUsername(String username) {
		return accountRepository.findByUsername(username).orElse(null);
	}

	@Override
	public List<Account> findAll() {
		return accountRepository.findAll();
	}

	@Override
	public Account login(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			Account account = accountRepository.findByUsername(loginRequest.getUsername()).orElse(null);
			if(account == null) throw new ResourceNotFoundException("Không tìm thấy tài khoản với tên đăng nhập là: " + loginRequest.getUsername());
			return account;
		} else {
			throw new AuthenticationException("Tài khoản hoặc mật khẩu không chính xác!");
		}
	}

	@Override
	public Account register(RegisterRequest registerRequest) {
		
		boolean emailExist = userService.checkEmailExist(registerRequest.getEmail());
		boolean phoneNumberExist = userService.checkPhoneNumberExist(registerRequest.getPhoneNumber());
		boolean usernameExist = accountRepository.existsByUsername(registerRequest.getUsername());
		
		if(emailExist) throw new ConflictException("Email đã được sử dụng!");
		if(phoneNumberExist) throw new ConflictException("Số điện thoại đã được sử dụng!");
		if(usernameExist) throw new ConflictException("Tên đăng nhập đã tồn tại!");
		
		Account account = new Account();
		account.setUsername(registerRequest.getUsername());
		account.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
		account.setRole(RoleAccount.USER);
		
		User user = new User();
		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setGender(Gender.valueOf(registerRequest.getGender()));
		user.setBirthday(registerRequest.getBirthday());
		user.setEmail(registerRequest.getEmail());
		user.setPhoneNumber(registerRequest.getPhoneNumber());
		user.setAccount(account);
		
		account.setUser(user);
		
		return accountRepository.save(account);
	}

	@Override
	public boolean isAccountVerifiedByEmail(boolean verify, String email) {
		return accountRepository.isAccountVerifiedByEmail(verify, email);
	}

	@Override
	public Account findByEmail(String email) {
		return accountRepository.findByEmail(email).orElse(null);
	}

	@Override
	public void verifyByEmail(String email) {
		Account account = accountRepository.findByEmail(email).orElse(null);
		if(account == null) throw new ResourceNotFoundException("Không tìm thấy tài khoản liên kết với email: " + email);
		account.setVerified(true);
		accountRepository.save(account);
	}

	@Override
	public boolean checkExistUsername(String username) {
		return accountRepository.existsByUsername(username);
	}

	@Override
	public Account updateAccount(Account account) {
		return accountRepository.save(account);
    }
}
