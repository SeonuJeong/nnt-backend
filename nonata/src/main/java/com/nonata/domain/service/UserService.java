package com.nonata.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nonata.domain.model.User;
import com.nonata.domain.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
	public void addOauth(User user) throws Exception {
    	userRepository.save(user);
    }
    
    public User findUserwithNickName(String nickname) {
    	User user = userRepository.findByNickname(nickname)
    			.orElse(null);
    	
    	return user;
    }
}
