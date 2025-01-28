package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.FriendShip;
import com.example.demo.entities.enums.FriendShipStatus;
import com.example.demo.entities.enums.FriendType;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, String> {

	@Query("select count(fs) > 0 from FriendShip fs join fs.user u join fs.friend f where (u.userId = :senderId and f.userId = :friendId) or (u.userId = :friendId and f.userId = :senderId)")
	boolean existsByUserAndFriend(@Param("senderId") String senderId, @Param("friendId") String friendId);

	@Query("select fo from User u join u.friendShips fo where u.userId = :userId and fo.status = 0")
	List<FriendShip> findByUserIdSendFriendRequest(@Param("userId") String userId);

	@Query("select fo from User u join u.friendOf fo where u.userId = :userId and fo.status = 0")
	List<FriendShip> findByUserIdReceiveFriendRequest(@Param("userId") String userId);

	@Query("select fs from FriendShip fs join fs.user u join fs.friend f where (u.userId = :userId or f.userId = :userId) and fs.status = :status")
	List<FriendShip> findByUserIdAndStatus(@Param("userId") String userId, @Param("status") FriendShipStatus status);

	@Query(value = "select " + "case " + "when u.user_id = :senderId then 'IS_YOU' "
			+ "when fs.friend_ship_id is null then 'NOT_FRIEND' "
			+ "when fs.friend_ship_id is not null and fs.status = 0 and fs.`user` = :senderId then 'REQUEST_SENT' "
			+ "when fs.friend_ship_id is not null and fs.status = 0 and fs.friend = :senderId then 'REQUEST_RECEIVED' "
			+ "when fs.friend_ship_id is not null and fs.status = 1 then 'FRIEND' else '???' " + "end AS friendType "
			+ "from user u left join friend_ship fs on u.user_id = fs.`user` "
			+ "or u.user_id = fs.friend where u.phone_number = :phoneNumber", nativeQuery = true)
	FriendType checkFriendTypeByPhoneNumberAndSenderId(@Param("phoneNumber") String phoneNumber,
			@Param("senderId") String senderId);

	@Query("select fs " + "from FriendShip fs " + "join fs.user u join fs.friend f " + "where (u.userId = :senderId "
			+ "and f.userId = :friendId) " + "or (u.userId = :friendId " + "and f.userId = :senderId)")
	Optional<FriendShip> findBySenderAndFriend(@Param("senderId") String senderId, @Param("friendId") String friendId);

}
