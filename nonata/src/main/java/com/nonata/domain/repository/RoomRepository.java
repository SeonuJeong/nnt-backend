package com.nonata.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nonata.domain.model.Room;
import com.nonata.domain.model.User;

public interface RoomRepository extends JpaRepository<Room, Long> {
	
	Optional<Room> findByHost(User host);
	
}
