package com.nonata.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class RoomInfoDto {
	LocationDto locationDto;
	int enteredNum;
	Long roomId;
}
