package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.FriendShip;
import com.example.demo.entities.enums.FriendShipStatus;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, String> {
	
	@Query("select count(fs) > 0 from FriendShip fs join fs.user u join fs.friend f where u.userId = :senderId and f.userId = :friendId")
	boolean existsByUserAndFriend(@Param("senderId") String senderId, @Param("friendId") String friendId);
	
	@Query("select fs from FriendShip fs join fs.user u where u.userId = :userId and fs.status = :status")
	List<FriendShip> findByUserIdAndStatusSend(@Param("userId") String userId, @Param("status") FriendShipStatus status);
	
	@Query("select fs from FriendShip fs join fs.friend f where f.userId = :userId and fs.status = :status")
	List<FriendShip> findByUserIdAndStatusReceive(@Param("userId") String userId, @Param("status") FriendShipStatus status);
	
	@Query("select fs from FriendShip fs join fs.user u join fs.friend f where u.userId = :userId or f.userId = :userId and fs.status = :status")
	List<FriendShip> findByUserIdAndStatus(@Param("userId") String userId, @Param("status") FriendShipStatus status);
	
}
