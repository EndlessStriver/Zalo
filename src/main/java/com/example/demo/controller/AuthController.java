package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EmailRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.OTPRequest;
import com.example.demo.dto.RefreshTokenResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.ResponseDataSuccess;
import com.example.demo.dto.ResponseError;
import com.example.demo.dto.ResponseErrorForm;
import com.example.demo.dto.ResponseSuccess;
import com.example.demo.entities.Account;
import com.example.demo.entities.enums.TokenType;
import com.example.demo.service.AccountService;
import com.example.demo.service.EmailService;
import com.example.demo.service.JwtService;
import com.example.demo.service.RedisService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private AccountService accountService;
	private EmailService emailService;
	private RedisService redisService;
	private JwtService jwtService;

	@Value("${spring.mail.username}")
	private String emailSystem;

	public AuthController(AccountService accountService, EmailService emailService, RedisService redisService,
			JwtService jwtService) {
		this.accountService = accountService;
		this.emailService = emailService;
		this.redisService = redisService;
		this.jwtService = jwtService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {

		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<String, String>();
			result.getFieldErrors().stream().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseErrorForm(HttpStatus.BAD_REQUEST.value(), "Đăng nhập thất bại", errors));
		}

		Account account = accountService.login(loginRequest);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<LoginResponse>(HttpStatus.OK.value(),
				"Đăng nhập thành công", convertAccountToLoginResponse(account)));
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult result) {

		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<String, String>();
			result.getFieldErrors().stream().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseErrorForm(HttpStatus.BAD_REQUEST.value(), "Đăng kí thất bại", errors));
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<Account>(HttpStatus.OK.value(),
				"Đăng ký thành công", accountService.register(registerRequest)));
	}

	@PostMapping("/send-otp")
	public ResponseEntity<?> verify(@Valid @RequestBody EmailRequest emailRequest, BindingResult result)
			throws MessagingException {

		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<String, String>();
			result.getFieldErrors().stream().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseErrorForm(HttpStatus.BAD_REQUEST.value(), "Gửi mã OTP thất bại", errors));
		}

		Integer otp = generateOTP();
		redisService.saveDataWithTTL(String.format("otp?email=%s", emailRequest.getEmail()), otp.toString(), 120);
		emailService.sendEmailVerifyAccount(emailSystem, emailRequest.getEmail(), otp.toString());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseSuccess(HttpStatus.OK.value(), "Mã OTP đã được gửi đến email của bạn"));
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOTP(@Valid @RequestBody OTPRequest otpRequest, BindingResult result) {

		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<String, String>();
			result.getFieldErrors().stream().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseErrorForm(HttpStatus.BAD_REQUEST.value(), "Xác thực tài khoản thất bại", errors));
		}

		if (accountService.checkAccountIsVerify(true, otpRequest.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Tài khoản đã được xác thực"));
		}

		Object otp = redisService.get(String.format("otp?email=%s", otpRequest.getEmail()));

		if (otp == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Mã OTP không đúng hoặc đã hết hạn"));
		}

		if (!((String) otp).equals(otpRequest.getOtp())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Mã OTP không đúng hoặc đã hết hạn"));
		}

		accountService.verifyAccountWithEmail(otpRequest.getEmail());
		redisService.delete(String.format("otp?email=%s", otpRequest.getEmail()));
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseSuccess(HttpStatus.OK.value(), "Xác thực tài khoản thành công"));
	}
	
	@GetMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshTokenHeader) {
		String refreshToken = refreshTokenHeader.substring(7);
		String accessToken = jwtService.refreshToken(refreshToken);
		RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(accessToken);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<RefreshTokenResponse>(
				HttpStatus.OK.value(), "Làm mới access token thành công", refreshTokenResponse));
	}

	private int generateOTP() {
		return (int) (Math.floor((Math.random() * 900000)) + 100000);
	}

	private LoginResponse convertAccountToLoginResponse(Account account) {

		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setAccountId(account.getAccountId());
		loginResponse.setUsername(account.getUsername());
		loginResponse.setRole(account.getRole());
		loginResponse.setActived(account.isActived());
		loginResponse.setVerified(account.isVerified());
		loginResponse.setUser(account.getUser());
		loginResponse.setCreatedAt(account.getCreatedAt());
		loginResponse.setUpdatedAt(account.getUpdatedAt());
		loginResponse.setAccessToken(jwtService.generateToken(account.getUsername(), TokenType.ACCESS));
		loginResponse.setRefreshToken(jwtService.generateToken(account.getUsername(), TokenType.REFRESH));

		return loginResponse;
	}

}
