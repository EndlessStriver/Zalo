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
			WHERE ucr.user.userId = :myId
			AND cr.roomName = :roomName
			""")
	List<ChatRoom> findByUserIdAndRoomName(@Param("myId") String myId, @Param("roomName") String roomName);
	
	@Query(value = """
				WITH LASTMESSAGE as (
				    SELECT ms.*, ROW_NUMBER() OVER (PARTITION BY ms.chat_room ORDER BY ms.timestamp DESC) AS rn
				    FROM message ms
				)
				
				SELECT cr.chat_room_id, cr.created_at, cr.updated_at, cr.type, cr.room_image, cr.room_name, lm.message_id, lm.timestamp, lm.chat_room, 
				       lm.user_id, lm.content, lm.image_url, lm.type_image, lm.file_url, lm.type_file 
				FROM chat_room cr
				JOIN user_chat_room ucr ON cr.chat_room_id = ucr.chat_room_id
				JOIN LASTMESSAGE lm on cr.chat_room_id = lm.chat_room
				WHERE ucr.user_id = :myId AND lm.rn = 1
			""", nativeQuery = true)
	List<ChatRoom> findMyChatRooms(@Param("myId") String myId);

}
