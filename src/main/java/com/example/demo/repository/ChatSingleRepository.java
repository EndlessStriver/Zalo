package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.ChatSingle;

@Repository
public interface ChatSingleRepository extends JpaRepository<ChatSingle, String> {

	@Query("""
			    SELECT cr FROM ChatRoom cr
			    WHERE cr.chatRoomId IN (
			        SELECT uc.chatRoom.chatRoomId FROM UserChatRoom uc
			        WHERE uc.user.userId IN (:myId, :friendId)
			        GROUP BY uc.chatRoom.chatRoomId
			        HAVING COUNT(uc.user.userId) = 2
			    )
			""")
	Optional<ChatSingle> findChatRoomForUsers(@Param("myId") String myId, @Param("friendId") String friendId);

}
