package com.nonata.domain.controller;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nonata.domain.model.OAuthToken;
import com.nonata.domain.model.User;
import com.nonata.domain.service.UserService;
import com.nonata.global.jwt.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

	private final JwtService jwtService;
	private final UserService userService; 
	
	@Value("${redirect}")
	private String redirictURL;
	
	@GetMapping(value={"/",""})
	public String home() {
		System.out.println("Getin");
		return "index.html";
	}
	
	@GetMapping("/auth/kakao/callback")
	public void kakaoCallback(String code,HttpServletResponse httpResponse) {

		
		RestTemplate rt = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "f660e8680bf32f3c0d2370ef3a50cf9d");
		params.add("redirect_uri", redirictURL+"/auth/kakao/callback");
		params.add("code", code);

		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

		ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,
				kakaoTokenRequest, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		
		OAuthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// =============사용자 데이터 요청=================

		RestTemplate rt2 = new RestTemplate();

		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

		ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
				kakaoProfileRequest, String.class);
		
		Map<String, Object> map = null;
		try {
			map = new ObjectMapper().readValue(response2.getBody(), Map.class) ;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        
		Map<String, Object> account = (Map<String, Object>) map.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (account == null || profile == null) {
            return;
        }

        System.out.println("사용자 닉네임:" + (String) profile.get("nickname")+"_"+map.get("id"));
         
        String email = UUID.randomUUID() + "@nonata.com";
		User user = User.builder()
				.email(email)
				.nickname((String) profile.get("nickname")+"_"+map.get("id"))				
				.build();
		
		User originUser = userService.findUserwithNickName(user.getNickname());
		
		if(originUser==null) {
			try {
				userService.addOauth(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			email = originUser.getEmail();
		}
		
		String accessToken = jwtService.createAccessToken(email);
		Cookie cookie = new Cookie(jwtService.getAccessHeader(), accessToken);
		cookie.setMaxAge(3600000);
		cookie.setPath("/");
		httpResponse.addCookie(cookie);
	
	}
}
