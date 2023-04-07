package com.nonata.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nonata.domain.dto.LocationDto;
import com.nonata.domain.model.Loc;
import com.nonata.domain.repository.LocRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LocService {

	private final LocRepository locRepository;

	public List<Loc> findRoom(LocationDto v) {

		double diffX = LatitudeInDifference(500);
		double diffstartY = LongitudeInDifference(v.getStartX(), 500);
		double diffdestY = LongitudeInDifference(v.getDestX(), 500);

		return locRepository.findAllByStartXBetweenAndStartYBetweenAndDestXBetweenAndDestYBetween(
				v.getStartX()-diffX,v.getStartX()+diffX,
				v.getStartY()-diffstartY, v.getStartY()+diffstartY,
				v.getDestX()-diffX,v.getDestX()+diffX,
				v.getDestY()-diffdestY, v.getDestY()+diffdestY
				);
	}

	// 반경 m이내의 위도차(degree)
	public double LatitudeInDifference(int diff) {
		// 지구반지름
		final int earth = 6371000; // 단위m

		return (diff * 360.0) / (2 * Math.PI * earth);
	}

	// 반경 m이내의 경도차(degree)
	public double LongitudeInDifference(double _latitude, int diff) {
		// 지구반지름
		final int earth = 6371000; // 단위m

		double ddd = Math.cos(0);
		double ddf = Math.cos(Math.toRadians(_latitude));

		return (diff * 360.0) / (2 * Math.PI * earth * Math.cos(Math.toRadians(_latitude)));
	}
}
