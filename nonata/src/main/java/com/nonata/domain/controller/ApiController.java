package com.nonata.domain.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nonata.domain.dto.LocationDto;
import com.nonata.domain.dto.ResponseDto;
import com.nonata.domain.dto.RoomInfoDto;
import com.nonata.domain.model.EnteredUser;
import com.nonata.domain.model.Loc;
import com.nonata.domain.model.Room;
import com.nonata.domain.model.User;
import com.nonata.domain.service.EnteredUserService;
import com.nonata.domain.service.LocService;
import com.nonata.domain.service.RoomService;
import com.nonata.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

	private final LocService locService;
	private final UserService userService;
	private final RoomService roomService;
	private final EnteredUserService enteredUserService;

	
	
	@GetMapping("/set")
	public ResponseDto<String> firstLogin() throws Exception {
		log.info("loginGETIN");
		return new ResponseDto<String>(HttpStatus.OK,"ok");
	}
	
	@PostMapping("/find")
	public List<RoomInfoDto> findRoom(@RequestBody LocationDto locationDto) throws Exception {
		List<Loc> locs = locService.findRoom(locationDto);

		List<RoomInfoDto> result = new ArrayList<>();

		for (Loc loc : locs) {

			if (distance(loc.getStartX(), loc.getStartY(), locationDto.getStartX(), locationDto.getStartY(),
					"meter") > locationDto.getStartRange()
					|| distance(loc.getDestX(), loc.getDestY(), locationDto.getDestX(), locationDto.getDestY(),
							"meter") > locationDto.getDestRange())
				continue;

			if(loc.getRoom().getEnteredUsers().size()>=4)
				continue;
			
			result.add(RoomInfoDto.builder()
					.locationDto(LocationDto.builder().startX(loc.getStartX()).startY(loc.getStartY())
							.destX(loc.getDestX()).destY(loc.getDestY()).startAddress(loc.getRoom().getStartPlace())
							.destAddress(loc.getRoom().getDestPlace()).build())
					.roomId(loc.getRoom().getId()).enteredNum(loc.getRoom().getEnteredUsers().size()).build());
		}

		return result;
	}

	@PostMapping("/make")
	public void makeRoom(@RequestBody LocationDto locationDto, @AuthenticationPrincipal UserDetails principal)
			throws Exception {

		User user = userService.findUserwithEmail(principal.getUsername());

		if (user == null)
			throw new Exception("존재하지 않는 유저입니다");

		if (!enteredUserService.checkEnteredUser(user)) {
			throw new Exception("이미 입장한 방이 존재합니다.");
		}

		Loc loc = Loc.builder().startX(locationDto.getStartX()).startY(locationDto.getStartY())
				.destX(locationDto.getDestX()).destY(locationDto.getDestY()).build();

		Room room = Room.builder().loc(loc).host(user).startPlace(locationDto.getStartAddress())
				.destPlace(locationDto.getDestAddress()).build();

		EnteredUser enteredUser = EnteredUser.builder().arrivalState(true).room(room).user(user).build();

		roomService.registerRoom(room, enteredUser);

	}

	@PostMapping("/enter")
	public void enterRoom(@RequestBody RoomInfoDto roomInfoDto, @AuthenticationPrincipal UserDetails principal)
			throws Exception {

		User user = userService.findUserwithEmail(principal.getUsername());

		if (user == null)
			throw new Exception("존재하지 않는 유저입니다");

		if (!enteredUserService.checkEnteredUser(user)) 
			throw new Exception("이미 입장한 방이 존재합니다.");
		
		
		roomService.enterIn(roomInfoDto.getRoomId(),user);
		
	}
	
	@GetMapping("/myroom")
	public ResponseDto<String> myRoom(@AuthenticationPrincipal UserDetails principal)
			throws Exception {

		User user = userService.findUserwithEmail(principal.getUsername());

		if (user == null)
			throw new Exception("존재하지 않는 유저입니다");

	
		if(!enteredUserService.checkEnteredUser(user)) {
			return new ResponseDto<String>(HttpStatus.ACCEPTED,"OK");
		}
		
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR,"NO");
		
	}
	
	@PostMapping("/getOutRoom")
	public ResponseDto<String> eraseRoom(@AuthenticationPrincipal UserDetails principal)
			throws Exception {

		User user = userService.findUserwithEmail(principal.getUsername());

		if (user == null)
			throw new Exception("존재하지 않는 유저입니다");

		
		enteredUserService.eraseRoom(user);
		
		return new ResponseDto<String>(HttpStatus.OK,"ok");
		
	}

	/**
	 * 두 지점간의 거리 계산
	 * 
	 * @param lat1 지점 1 위도
	 * @param lon1 지점 1 경도
	 * @param lat2 지점 2 위도
	 * @param lon2 지점 2 경도
	 * @param unit 거리 표출단위
	 * @return
	 */
	private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		if (unit == "kilometer") {
			dist = dist * 1.609344;
		} else if (unit == "meter") {
			dist = dist * 1609.344;
		}

		return (dist);
	}

	// This function converts decimal degrees to radians
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	// This function converts radians to decimal degrees
	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}
