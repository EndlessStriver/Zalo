package com.example.demo.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.FriendShip;
import com.example.demo.entities.User;
import com.example.demo.entities.enums.FriendShipStatus;
import com.example.demo.exception.AuthorizationException;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FriendShipRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FriendShipService;

@Service
public class FriendShipServiceImp implements FriendShipService {

	private UserRepository userRepository;
	private FriendShipRepository friendShipRepository;

	public FriendShipServiceImp(UserRepository userRepository, FriendShipRepository friendShipRepository) {
		this.userRepository = userRepository;
		this.friendShipRepository = friendShipRepository;
	}

	@Override
	public FriendShip sendFriendRequest(String senderId, String friendId) {
		User sender = userRepository.findById(senderId).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy người gửi kết bạn với id là: " + senderId));

		User friend = userRepository.findById(friendId).orElseThrow(() -> new ResourceNotFoundException(
				"Không tìm thấy người nhận lời mời kết bạn với id là: " + friendId));
		
		if(sender.getUserId().equals(friend.getUserId()))
            throw new BadRequestException("Bạn không thể gửi yêu cầu kết bạn cho chính mình");

		boolean checkFriendShipIsExists = friendShipRepository.existsByUserAndFriend(senderId, friendId);

		if (checkFriendShipIsExists)
			throw new BadRequestException("Bạn đã gửi kết bạn hoặc đang là bạn bè với người dùng này");

		FriendShip friendShip = new FriendShip();
		friendShip.setUser(sender);
		friendShip.setFriend(friend);

		return friendShipRepository.save(friendShip);
	}

	@Override
	public FriendShip acceptFriendRequest(String senderId, String friendShipId) {
		FriendShip friendShip = friendShipRepository.findById(friendShipId).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy yêu cầu kết bạn với id là: " + friendShipId));

		if (!friendShip.getUser().getUserId().equals(senderId))
			throw new AuthorizationException("Bạn không thể chấp nhận yêu cầu kết bạn của người khác");
		
		if (friendShip.getStatus() != FriendShipStatus.PENDING)
			throw new BadRequestException("Yêu cầu kết bạn này đã được xử lý");

		friendShip.setStatus(FriendShipStatus.ACCEPTED);

		return friendShipRepository.save(friendShip);
	}

	@Override
	public void cancelFriendship(String senderId, String friendShipId) {
		FriendShip friendShip = friendShipRepository.findById(friendShipId).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy yêu cầu kết bạn với id là: " + friendShipId));

		if (friendShip.getUser().getUserId().equals(senderId) || friendShip.getFriend().getUserId().equals(senderId))
			friendShipRepository.delete(friendShip);
		else
			throw new AuthorizationException("Bạn không thể hủy yêu cầu kết bạn của người khác");
	}

	@Override
	public List<FriendShip> getSendedFriendRequest(String senderId) {
		return friendShipRepository.findByUserIdAndStatusSend(senderId, FriendShipStatus.PENDING);
	}

	@Override
	public List<FriendShip> getReceivedFriendRequest(String receiverId) {
		return friendShipRepository.findByUserIdAndStatusReceive(receiverId, FriendShipStatus.PENDING);
	}

	@Override
	public List<FriendShip> getFriendList(String userId) {
		return friendShipRepository.findByUserIdAndStatus(userId, FriendShipStatus.ACCEPTED);
	}

}
