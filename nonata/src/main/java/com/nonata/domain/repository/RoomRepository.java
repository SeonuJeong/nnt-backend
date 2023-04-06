package com.nonata.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nonata.domain.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
