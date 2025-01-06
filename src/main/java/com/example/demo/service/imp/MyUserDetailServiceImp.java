package com.example.demo.service.imp;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Account;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.MyUserDetailService;

@Service
public class MyUserDetailServiceImp implements MyUserDetailService {

	private AccountRepository accountRepository;

	public MyUserDetailServiceImp(AccountRepository accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.getAccountByUsername(username).orElse(null);
		if (account == null)
			throw new ResourceNotFoundException("Tài khoản hoăc mật khẩu không chính xác");
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + account.getRole());
		UserDetails userDetails = User.withUsername(account.getUsername()).password(account.getPassword())
				.authorities(authority).build();
		return userDetails;
	}

}
