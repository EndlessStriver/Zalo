package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProfileUpdateRequest;
import com.example.demo.dto.ResponseDataSuccess;
import com.example.demo.dto.ResponseErrorForm;
import com.example.demo.entities.Account;
import com.example.demo.entities.User;
import com.example.demo.entities.enums.Gender;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.UserService;
import com.example.demo.unit.MethodUnit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private UserService userService;
	private MethodUnit methodUnit;

	public UserController(UserService userService, MethodUnit methodUnit) {
		this.userService = userService;
		this.methodUnit = methodUnit;
	}

	@GetMapping("/contacts")
	public ResponseEntity<?> getFriendsAndMessageContacts(@RequestParam String friendName, HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		List<User> users = userService.getFriendsAndMessageContacts(sender.getUser().getUserId(), friendName);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<List<User>>(HttpStatus.OK.value(),
				"Lấy danh sách người dùng đã tương tác thành công", users));
	}

	@GetMapping("/search-by-phone")
	public ResponseEntity<?> getUserByPhoneNumber(@RequestParam String phoneNumber, HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		User user = userService.findByPhoneNumber(phoneNumber, sender.getUser().getUserId());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDataSuccess<User>(HttpStatus.OK.value(), "Tìm bạn bè thành công", user));
	}

	@PatchMapping("/profile")
	public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileUpdateRequest myProfile, BindingResult result,
			HttpServletRequest request) {
		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<String, String>();
			result.getFieldErrors().stream().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseErrorForm(HttpStatus.BAD_REQUEST.value(), "Cập nhật thông tin thất bại", errors));
		}
		
		boolean emailExist = userService.checkEmailExist(myProfile.getEmail());
		boolean phoneNumberExist = userService.checkPhoneNumberExist(myProfile.getPhoneNumber());
		
		Account sender = methodUnit.getAccountFromToken(request);
		User user = userService.findById(sender.getUser().getUserId());
		if (user == null) throw new ResourceNotFoundException("Người dùng cần cập nhật không tồn tại");
		if(emailExist && (!myProfile.getEmail().equals(user.getEmail()))) throw new ConflictException("Email đã được sử dụng!");
		if(phoneNumberExist && (!myProfile.getPhoneNumber().equals(user.getPhoneNumber()))) throw new ConflictException("Số điện thoại đã được sử dụng!");
		user.setFirstName(myProfile.getFirstName());
		user.setLastName(myProfile.getLastName());
		user.setGender(Gender.valueOf(myProfile.getGender()));
		user.setBirthday(myProfile.getBirthday());
		user.setEmail(myProfile.getEmail());
		user.setPhoneNumber(myProfile.getPhoneNumber());
		userService.updateProfile(user);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDataSuccess<User>(HttpStatus.OK.value(), "Cập nhật thông tin thành công", user));
	}
}
