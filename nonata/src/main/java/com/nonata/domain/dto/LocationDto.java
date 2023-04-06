package com.nonata.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class LocationDto {
	private Double startX;
	private Double startY;
	private Double destX;
	private Double destY;
	private String startAddress;
	private String destAddress;
	private int startRange;
	private int destRange;
}
