package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDataSuccess;
import com.example.demo.dto.ResponseSuccess;
import com.example.demo.entities.Account;
import com.example.demo.entities.FriendShip;
import com.example.demo.entities.enums.FriendType;
import com.example.demo.service.FriendShipService;
import com.example.demo.unit.MethodUnit;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/friendships")
public class FriendShipController {

	private FriendShipService friendShipService;
	private MethodUnit methodUnit;

	public FriendShipController(FriendShipService friendShipService, MethodUnit methodUnit) {
		this.friendShipService = friendShipService;
		this.methodUnit = methodUnit;
	}

	@GetMapping
	public ResponseEntity<?> getFriendList(HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		List<FriendShip> friendShips = friendShipService.getFriendList(sender.getUser().getUserId());
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<List<FriendShip>>(
				HttpStatus.OK.value(), "Lấy danh sách bạn bè thành công", friendShips));
	}

	@GetMapping("/check-friendship")
	public ResponseEntity<?> checkFriendshipByPhoneNumber(@RequestParam String phoneNumber,
			HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		FriendType friendType = friendShipService.checkFriendshipByPhoneNumber(phoneNumber,
				sender.getUser().getUserId());
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<FriendType>(HttpStatus.OK.value(),
				"Kiểm tra quan hệ bạn bè thành công", friendType));
	}

	@PostMapping
	public ResponseEntity<?> sendFriendRequest(@RequestParam String friendId, HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		FriendShip friendShip = friendShipService.sendFriendRequest(sender.getUser().getUserId(), friendId);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDataSuccess<FriendShip>(
				HttpStatus.CREATED.value(), "Gửi lời mời kết bạn thành công", friendShip));
	}

	@PatchMapping
	public ResponseEntity<?> acceptFriendRequest(@RequestParam String friendId, HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		FriendShip friendShip = friendShipService.acceptFriendRequest(sender.getUser().getUserId(), friendId);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<FriendShip>(HttpStatus.OK.value(),
				"Chấp nhận lời mời kết bạn thành công", friendShip));
	}

	@DeleteMapping
	public ResponseEntity<?> cancelFriendship(@RequestParam String friendId, HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		friendShipService.cancelFriendship(sender.getUser().getUserId(), friendId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseSuccess(HttpStatus.OK.value(), "Hủy kết bạn thành công"));
	}

	@GetMapping("/sent-requests")
	public ResponseEntity<?> getFriendRequests(HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		List<FriendShip> friendShips = friendShipService.getSendedFriendRequest(sender.getUser().getUserId());
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<List<FriendShip>>(
				HttpStatus.OK.value(), "Lấy danh sách đã mời kết bạn thành công", friendShips));
	}

	@GetMapping("/received-requests")
	public ResponseEntity<?> getReceivedFriendRequests(HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		List<FriendShip> friendShips = friendShipService.getReceivedFriendRequest(sender.getUser().getUserId());
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<List<FriendShip>>(
				HttpStatus.OK.value(), "Lấy danh sách được yêu cầu kết bạn thành công", friendShips));
	}

}
