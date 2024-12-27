package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EmailRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.OTPRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.ResponseSuccess;
import com.example.demo.entities.Account;
import com.example.demo.service.AccountService;
import com.example.demo.service.EmailService;
import com.example.demo.service.RedisService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private AccountService accountService;
	private EmailService emailService;
	private RedisService redisService;

	@Value("${spring.mail.username}")
	private String emailSystem;

	public AuthController(AccountService accountService, EmailService emailService, RedisService redisService) {
		this.accountService = accountService;
		this.emailService = emailService;
		this.redisService = redisService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseSuccess<Account>(HttpStatus.OK.value(),
				"Đăng nhập thành công", accountService.login(loginRequest)));
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseSuccess<Account>(HttpStatus.OK.value(),
				"Đăng ký thành công", accountService.register(registerRequest)));
	}

	@PostMapping("/send-otp")
	public ResponseEntity<?> verify(@RequestBody EmailRequest emailRequest) throws MessagingException {
		Integer otp = generateOTP();
		redisService.saveDataWithTTL(String.format("otp?email=%s", emailRequest.getEmail()), otp.toString(), 120);
		emailService.sendEmailVerifyAccount(emailSystem, emailRequest.getEmail(), otp.toString());
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseSuccess<String>(HttpStatus.OK.value(),
				"Gửi mã OTP thành công", "Mã OTP đã được gửi đến email của bạn"));
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOTP(@RequestBody OTPRequest otpRequest) {
		
		if(accountService.checkAccountIsVerify(true, otpRequest.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseSuccess<String>(HttpStatus.BAD_REQUEST.value(), "Xác thực tài khoản thất bại",
							"Tài khoản đã được xác thực"));
		}
		
		Object otp = redisService.get(String.format("otp?email=%s", otpRequest.getEmail()));

		if (otp == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseSuccess<String>(HttpStatus.BAD_REQUEST.value(), "Xác thực tài khoản thất bại",
							"Mã OTP không đúng hoặc đã hết hạn"));
		}
		
		if(Integer.parseInt((String) otp) != otpRequest.getOtp()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseSuccess<String>(HttpStatus.BAD_REQUEST.value(), "Xác thực tài khoản thất bại",
							"Mã OTP không đúng hoặc đã hết hạn"));
		}
		
		accountService.verifyAccountWithEmail(otpRequest.getEmail());
		redisService.delete(String.format("otp?email=%s", otpRequest.getEmail()));
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseSuccess<String>(HttpStatus.OK.value(),
				"Xác thực tài khoản thành công", "Tài khoản của bản được xác thực"));
	}

	private int generateOTP() {
		return (int) (Math.floor((Math.random() * 900000)) + 100000);
	}

}
