package com.example.demo.entity;

import com.fasterxml.jackson.annotation.*;

public enum Level {
	BRONZE("일반회원"), SILVER("실버회원"), GOLD("골드회원");
	
	@JsonValue
	private String korean;
	
	Level(String korean) {
		this.korean = korean;
	}
}
