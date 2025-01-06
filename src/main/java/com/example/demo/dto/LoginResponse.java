package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.entities.User;
import com.example.demo.entities.enums.RoleAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
	private String accountId;
	private String username;
	private RoleAccount role;
	private boolean actived;
	private boolean verified;
	private User user;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String accessToken;
}
