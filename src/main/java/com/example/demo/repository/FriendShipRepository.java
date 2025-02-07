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

	@Query("""
			    SELECT COALESCE((
				    SELECT CASE
				        WHEN (u.userId = :myId AND f.userId = :friendId AND fs.status = 1) 
				          OR (f.userId = :myId AND u.userId = :friendId AND fs.status = 1) THEN 'FRIEND'
				        WHEN u.userId = :myId AND f.userId = :friendId AND fs.status = 0 THEN 'REQUEST_SENT'
				        WHEN u.userId = :friendId AND f.userId = :myId AND fs.status = 0 THEN 'REQUEST_RECEIVED'
				        ELSE 'NOT_FRIEND'
				    END
				    FROM FriendShip fs
				    JOIN fs.user u
				    JOIN fs.friend f
				    WHERE (u.userId = :myId OR f.userId = :myId)
				    AND (u.userId = :friendId OR f.userId = :friendId)
				), 'NOT_FRIEND') AS friendship_status
			""")
	FriendType checkFriendTypeByPhoneNumberAndSenderId(@Param("myId") String myId, @Param("friendId") String friendId);

	@Query("""
				SELECT fs FROM FriendShip fs
				JOIN fs.user u
				JOIN fs.friend f
				WHERE (u.userId = :senderId AND f.userId = :friendId)
				OR (u.userId = :friendId AND f.userId = :senderId)
			""")
	Optional<FriendShip> findBySenderAndFriend(@Param("senderId") String senderId, @Param("friendId") String friendId);

}
