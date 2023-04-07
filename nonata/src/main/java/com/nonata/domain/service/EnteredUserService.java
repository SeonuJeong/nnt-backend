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
public class EnteredUserService {

	private final EnteredUserRepository enteredUserRepository;

	private final RoomRepository roomRepository;
	
	public boolean checkEnteredUser(User user) throws Exception {

		if (enteredUserRepository.findByUser(user).isEmpty()) {
			return true;
		}

		return false;
	}
	
	public void eraseRoom(User user) throws Exception {
		
		
		Room room = roomRepository.findByHost(user).orElse(null);
		EnteredUser enteredUser = enteredUserRepository.findByUser(user).orElse(null);
		
	
		
		long userId=user.getId();
		
		if(enteredUser!=null) {
			enteredUser.setUser(null);
			enteredUserRepository.save(enteredUser);
			enteredUserRepository.deleteById(enteredUser.getId());
		}
		
		
		if(room!=null&& room.getHost().getId()==userId) {
			room.setHost(null);
			roomRepository.save(room);
			roomRepository.deleteById(room.getId());
		}
		
	}
	
	
}