package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	@Query("""
				SELECT COUNT(u) > 0
				FROM User u
				WHERE u.email = :email
			""")
	boolean checkEmailIsExists(@Param("email") String email);

	@Query(value = """
				SELECT u.*
				FROM friend_ship fs
				JOIN user u ON (fs.friend = u.user_id OR fs.user = u.user_id)
				WHERE (fs.friend = :userId OR fs.user = :userId)
				AND u.user_id != :userId
				AND LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :friendName, '%'))

				UNION

				SELECT DISTINCT u.*
				FROM user_chat_room ucr1
				JOIN user_chat_room ucr2 ON ucr1.user_chat_room_id = ucr2.user_chat_room_id
				JOIN user u ON ucr2.user_id = u.user_id
				JOIN chat_room cr ON ucr2.chat_room_id = cr.chat_room_id
				WHERE cr.type = 'chat_single'
				AND ucr1.user_id = :userId
				AND ucr2.user_id <> :userId
				AND LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :friendName, '%'))
			""", nativeQuery = true)
	List<User> findFriendsByFullnameAndUserId(@Param("userId") String userId, @Param("friendName") String friendName);

	@Query("""
				SELECT u FROM User u
				WHERE u.phoneNumber = :phoneNumber
			""")
	Optional<User> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}
