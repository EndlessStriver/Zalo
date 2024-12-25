package com.example.demo.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entities.enums.Gender;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
	
	@Id
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;
	
	@Column(name = "birthday")
	private LocalDate birthday;
	
	@Column(name = "phone_number", unique = true)
	private String phoneNumber;
	
	@Column(name = "email", unique = true)
	private String email;
	
	@Column(name = "bio")
	private String bio;
	
	@Column(name = "avatar_url")
	private String avatarUrl;
	
	@OneToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<FriendShip> friendShips;
	
	@OneToMany(mappedBy = "friend", cascade = CascadeType.ALL)
	private List<FriendShip> friendOf;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserChatRoom> userChatRooms;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}
