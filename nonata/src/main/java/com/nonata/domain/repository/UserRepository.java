package com.nonata.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nonata.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
