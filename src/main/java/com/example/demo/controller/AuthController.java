package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EmailRequest;
import com.example.demo.dto.ForgotPasswordRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.OTPRequest;
import com.example.demo.dto.RefreshTokenResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.ResponseDataSuccess;
import com.example.demo.dto.ResponseError;
import com.example.demo.dto.ResponseErrorForm;
import com.example.demo.dto.ResponseSuccess;
import com.example.demo.entities.Account;
import com.example.demo.entities.enums.TokenType;
import com.example.demo.exception.AuthenticationException;
import com.example.demo.service.AccountService;
import com.example.demo.service.EmailService;
import com.example.demo.service.JwtService;
import com.example.demo.service.RedisService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private AccountService accountService;
	private EmailService emailService;
	private RedisService redisService;
	private JwtService jwtService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Value("${spring.mail.username}")
	private String emailSystem;

	public AuthController(AccountService accountService, EmailService emailService, RedisService redisService,
			JwtService jwtService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.accountService = accountService;
		this.emailService = emailService;
		this.redisService = redisService;
		this.jwtService = jwtService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@PatchMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest,
			BindingResult result) throws MessagingException {

		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<String, String>();
			result.getFieldErrors().stream().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ResponseErrorForm(HttpStatus.BAD_REQUEST.value(), "Đổi mật khẩu không thành công", errors));
		}

		Account account = accountService.findByEmail(forgotPasswordRequest.getEmail());
		String myOTP = (String) redisService.get(String.format("otp?email=%s", forgotPasswordRequest.getEmail()));

		if (!forgotPasswordRequest.getOtp().equals(myOTP)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Mã OTP không đúng hoặc đã hết hạn"));
		}

		account.setPassword(bCryptPasswordEncoder.encode(forgotPasswordRequest.getPassword()));
		Account newAccount = accountService.updateAccount(account);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDataSuccess<Account>(HttpStatus.OK.value(), "Đổi mật khẩu thành công", newAccount));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result,
			HttpServletResponse httpServletResponse) {

		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<String, String>();
			result.getFieldErrors().stream().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseErrorForm(HttpStatus.BAD_REQUEST.value(), "Đăng nhập thất bại", errors));
		}

		Account account = accountService.login(loginRequest);
		String accessToken = jwtService.generateToken(account.getUsername(), TokenType.ACCESS);
		String refreshToken = jwtService.generateToken(account.getUsername(), TokenType.REFRESH);
		account.setAccessToken(accessToken);

		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(false); // đang không gửi qua https
		cookie.setPath("/");
		cookie.setMaxAge(1 * 24 * 60 * 60); // Tồn tại trong 1 ngày

		httpServletResponse.addCookie(cookie);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDataSuccess<Account>(HttpStatus.OK.value(), "Đăng nhập thành công", account));
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse httpServletResponse) {

		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(false); // đang không gửi qua https
		cookie.setPath("/");
		cookie.setMaxAge(0); // Cho thời gian sống của cookies này bằng 0

		httpServletResponse.addCookie(cookie);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseSuccess(HttpStatus.OK.value(), "Đăng xuất thành công"));
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

		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDataSuccess<Account>(
				HttpStatus.CREATED.value(), "Đăng ký thành công", accountService.register(registerRequest)));
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

		if (accountService.isAccountVerifiedByEmail(true, otpRequest.getEmail())) {
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

		accountService.verifyByEmail(otpRequest.getEmail());
		redisService.delete(String.format("otp?email=%s", otpRequest.getEmail()));
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseSuccess(HttpStatus.OK.value(), "Xác thực tài khoản thành công"));
	}

	@GetMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(HttpServletRequest httpServletRequest) throws AuthenticationException {
		String refreshToken = null;

		if (httpServletRequest.getCookies() == null)
			throw new AuthenticationException("Refresh Token không hợp lệ hoặc đã hết hạn");

		for (Cookie cookie : httpServletRequest.getCookies()) {
			if ("refreshToken".equals(cookie.getName())) {
				refreshToken = cookie.getValue();
				break;
			}
		}

		if (refreshToken == null)
			throw new AuthenticationException("Refresh Token không hợp lệ hoặc đã hết hạn");

		String accessToken = jwtService.refreshToken(refreshToken);
		RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(accessToken);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<RefreshTokenResponse>(
				HttpStatus.OK.value(), "Làm mới access token thành công", refreshTokenResponse));
	}

	private int generateOTP() {
		return (int) (Math.floor((Math.random() * 900000)) + 100000);
	}

}
