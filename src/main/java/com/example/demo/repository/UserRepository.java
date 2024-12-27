package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	@Query("select count(u) > 0 from User u where u.email = :email")
	boolean checkEmailIsExists(@Param("email") String email);
	
}
