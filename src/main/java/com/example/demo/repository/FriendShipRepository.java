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

	@Query("""
				SELECT COUNT(fs) > 0
				FROM FriendShip fs
				JOIN fs.user u
				JOIN fs.friend f
				WHERE (u.userId = :senderId AND f.userId = :friendId)
				OR (u.userId = :friendId AND f.userId = :senderId)
			""")
	boolean existsByUserAndFriend(@Param("senderId") String senderId, @Param("friendId") String friendId);

	@Query("""
				SELECT fo FROM User u
				JOIN u.friendShips fo
				WHERE u.userId = :userId AND fo.status = 0
			""")
	/*
	 * Status = 0 là đang chờ được chấp nhận
	 */
	List<FriendShip> findByUserIdSendFriendRequest(@Param("userId") String userId);

	@Query("""
				SELECT fo FROM User u
				JOIN u.friendOf fo
				WHERE u.userId = :userId AND fo.status = 0
			""")
	/*
	 * Status = 0 là đang chờ được chấp nhận
	 */
	List<FriendShip> findByUserIdReceiveFriendRequest(@Param("userId") String userId);

	@Query("""
				SELECT fs FROM FriendShip fs
				JOIN fs.user u
				JOIN fs.friend f
				WHERE (u.userId = :userId OR f.userId = :userId)
				AND fs.status = :status
			""")
	List<FriendShip> findByUserIdAndStatus(@Param("userId") String userId, @Param("status") FriendShipStatus status);

	@Query(value = """
				SELECT CASE
				WHEN u.user_id = :senderId THEN 'IS_YOU'
				WHEN fs.friend_ship_id IS NULL THEN 'NOT_FRIEND'
				WHEN fs.friend_ship_id IS NOT NULL AND fs.status = 0 AND fs.`user` = :senderId THEN 'REQUEST_SENT'
				WHEN fs.friend_ship_id IS NOT NULL AND fs.status = 0 AND fs.friend = :senderId THEN 'REQUEST_RECEIVED'
				WHEN fs.friend_ship_id IS NOT NULL AND fs.status = 1 THEN 'FRIEND'
				ELSE '???'
				END AS friendType
				FROM user u
				LEFT JOIN friend_ship fs ON u.user_id = fs.`user` OR u.user_id = fs.friend
				WHERE u.phone_number = :phoneNumber
			""", nativeQuery = true)
	FriendType checkFriendTypeByPhoneNumberAndSenderId(@Param("phoneNumber") String phoneNumber,
			@Param("senderId") String senderId);

	@Query("""
				SELECT fs FROM FriendShip fs
				JOIN fs.user u
				JOIN fs.friend f
				WHERE (u.userId = :senderId AND f.userId = :friendId)
				OR (u.userId = :friendId AND f.userId = :senderId)
			""")
	Optional<FriendShip> findBySenderAndFriend(@Param("senderId") String senderId, @Param("friendId") String friendId);

}
