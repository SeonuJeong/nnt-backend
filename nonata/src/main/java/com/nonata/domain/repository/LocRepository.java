package com.nonata.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nonata.domain.model.Loc;

public interface LocRepository extends JpaRepository<Loc, Long> {

}
