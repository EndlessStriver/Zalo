package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
	
	@Query("select ac from Account ac where ac.username = :username")
	Optional<Account> getAccountByUsername(@Param("username") String username);
	
	@Query("select case when count(ac) > 0 then true else false end from Account ac join ac.user u where ac.verified = :verify and u.email = :email")
	boolean checkAccountIsVerify(@Param("verify") boolean verify, @Param("email") String email);
	
	@Query("select ac from Account ac join ac.user u where u.email = :email")
	Optional<Account> getAccountByEmail(@Param("email") String email);
	
}
