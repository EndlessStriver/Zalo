package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPRequest {
	
	@NotBlank(message = "Email không được để trống")
	@Email(message = "Email không hợp lệ")
	private String email;
	
	@NotBlank(message = "Mã OTP không được để trống")
	@Pattern(regexp = "^[0-9]{6,6}$", message = "Mã OTP không hợp lệ")
	private String otp;
	
}
