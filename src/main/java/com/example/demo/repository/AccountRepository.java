package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

	@Query("""
				SELECT ac FROM Account ac
				WHERE ac.username = :username
			""")
	Optional<Account> findByUsername(@Param("username") String username);

	@Query("""
				SELECT CASE WHEN count(ac) > 0
					THEN true
					ELSE false
				END
				FROM Account ac
				JOIN ac.user u
				WHERE ac.verified = :verify
				AND u.email = :email
			""")
	boolean isAccountVerifiedByEmail(@Param("verify") boolean verify, @Param("email") String email);

	@Query("""
				SELECT ac
				FROM Account ac
				JOIN ac.user u
				WHERE u.email = :email
			""")
	Optional<Account> findByEmail(@Param("email") String email);

}
