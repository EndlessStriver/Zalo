package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
	
	@Query("select c from ChatRoom c "
			+ "where c.roomName like lower(concat('%', :roomName, '%')) "
			+ "and exists (select 1 from c.userChatRooms ucr where ucr.user.userId = :userId)")
	List<ChatRoom> findRoomsByNameAndUserId(@Param("userId") String userId, @Param("roomName") String roomName);
	
}
