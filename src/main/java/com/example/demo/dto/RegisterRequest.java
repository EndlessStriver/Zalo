package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.entities.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Gender gender;
	private LocalDate birthday;
	private String email;
}
