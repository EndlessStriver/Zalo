package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.FriendShip;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, String> {

}
