package com.kyiminhan.spring.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public enum Authority {

	USER(0, "User"), MANAGER(1, "Manager"), ADMIN(2, "Admin");

	@Getter
	@Setter
	private int id;
	@Getter
	@Setter
	private String authority;
}