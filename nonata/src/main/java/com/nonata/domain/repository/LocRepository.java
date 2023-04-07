package com.nonata.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nonata.domain.model.Loc;

public interface LocRepository extends JpaRepository<Loc, Long> {

	List<Loc> findAllByStartXBetweenAndStartYBetweenAndDestXBetweenAndDestYBetween
	(double sxl, double sxh, double syl,double syh,
			double dxl, double dxh, double dyl, double dyh);
	
}
