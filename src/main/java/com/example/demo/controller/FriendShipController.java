package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.FriendShipRequest;
import com.example.demo.dto.ResponseDataSuccess;
import com.example.demo.dto.ResponseErrorForm;
import com.example.demo.dto.ResponseSuccess;
import com.example.demo.entities.Account;
import com.example.demo.entities.FriendShip;
import com.example.demo.service.FriendShipService;
import com.example.demo.unit.MethodUnit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

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

	@PostMapping
	public ResponseEntity<?> sendFriendRequest(@Valid @RequestBody FriendShipRequest friendShipRequest,
			BindingResult result, HttpServletRequest request) {

		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<String, String>();
			result.getFieldErrors().stream().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseErrorForm(
					HttpStatus.BAD_REQUEST.value(), "Gửi lời mời kết bạn không thành công", errors));
		}

		Account sender = methodUnit.getAccountFromToken(request);
		FriendShip friendShip = friendShipService.sendFriendRequest(sender.getUser().getUserId(),
				friendShipRequest.getFriendId());

		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDataSuccess<FriendShip>(
				HttpStatus.CREATED.value(), "Gửi lời mời kết bạn thành công", friendShip));
	}

	@PatchMapping("/{friendShipId}")
	public ResponseEntity<?> acceptFriendRequest(@PathVariable String friendShipId, HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		FriendShip friendShip = friendShipService.acceptFriendRequest(sender.getUser().getUserId(), friendShipId);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<FriendShip>(HttpStatus.OK.value(),
				"Chấp nhận lời mời kết bạn thành công", friendShip));
	}

	@DeleteMapping("/{friendShipId}")
	public ResponseEntity<?> cancelFriendship(@PathVariable String friendShipId, HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		friendShipService.cancelFriendship(sender.getUser().getUserId(), friendShipId);

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
