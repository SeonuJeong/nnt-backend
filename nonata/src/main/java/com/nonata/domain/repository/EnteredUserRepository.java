package com.nonata.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nonata.domain.model.EnteredUser;
import com.nonata.domain.model.User;

public interface EnteredUserRepository extends JpaRepository<EnteredUser, Long> {
	Optional<EnteredUser> findByUser(User user);
}
