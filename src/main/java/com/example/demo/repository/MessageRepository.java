package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Message;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
	
	@Query(value = """
				WITH ranked_messages AS (
				    SELECT m.*, ROW_NUMBER() OVER (PARTITION BY m.chat_room ORDER BY m.timestamp DESC) AS r
				    FROM message m
				    WHERE m.chat_room = :chatRoomId
				)
				SELECT *
				FROM ranked_messages
				Where r = 1
			""", nativeQuery = true)
	Optional<Message> findNewMessageByChatRoomId(@Param("chatRoomId") String chatRoomId);

}
