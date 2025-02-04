package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

	@Query("""
			SELECT cr FROM ChatRoom cr 
			JOIN cr.userChatRooms ucr 
			WHERE ucr.user.userId = :userId
			AND cr.roomName = :roomName
			""")
	List<ChatRoom> findByUserIdAndRoomName(@Param("userId") String userId, @Param("roomName") String roomName);

}
