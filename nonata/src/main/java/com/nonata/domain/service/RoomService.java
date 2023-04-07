package com.nonata.domain.service;

import org.springframework.stereotype.Service;

import com.nonata.domain.model.EnteredUser;
import com.nonata.domain.model.Room;
import com.nonata.domain.model.User;
import com.nonata.domain.repository.EnteredUserRepository;
import com.nonata.domain.repository.RoomRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final EnteredUserRepository enteredUserRepository;
	
	public void registerRoom(Room room,EnteredUser enteredUser) throws Exception {

		roomRepository.save(room);
		enteredUserRepository.save(enteredUser);
    }
	
	public void enterIn(long id,User user) throws Exception {

		Room room =  roomRepository.findById(id).orElseGet(() -> {
			return null;
		});
		
		if(room==null)
			throw new Exception("존재하지 않은 방입니다.");
		
		if(room.getEnteredUsers().size()>=4) 
			throw new Exception("인원초과 입니다.");
		
		EnteredUser enteredUser = EnteredUser.builder()
									.user(user)
									.room(room)
									.arrivalState(false)
									.build();
		
		enteredUserRepository.save(enteredUser);
    }
	
}
