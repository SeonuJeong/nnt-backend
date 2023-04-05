package com.nonata.domain.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
public class Room {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "roomId")
	private Long Id;
	
	private String startPlace;
	
	private String destPlace;
	
	@OneToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name="locId")
	private Loc loc;
	
	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY) // mappedBy 연관관계의 주인이 아니다(난 FK가 아니에요) DB에 컬럼을 만들지 마세요.
	@JsonIgnoreProperties({"room"})
	private List<EnteredUser> enteredUsers; // DB는 오브젝트를 사용할 수 없다, FK. 자바는 오브젝트를 저장할 수 있다.
	
	
	private Integer price;
	
	@OneToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name="userId")
	private User host;
	
}
