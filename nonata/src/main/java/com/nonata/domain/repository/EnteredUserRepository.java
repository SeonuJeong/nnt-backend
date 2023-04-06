package com.nonata.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nonata.domain.model.EnteredUser;

public interface EnteredUserRepository extends JpaRepository<EnteredUser, Long> {

}
