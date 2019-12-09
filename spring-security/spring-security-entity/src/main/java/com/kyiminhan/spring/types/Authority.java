package com.kyiminhan.spring.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public enum Authority {

	USER(0, "User", "ROLE_USER"), MANAGER(1, "Manager", "ROLE_MANAGER"), ADMIN(2, "Admin", "ROLE_ADMIN");

	@Getter
	@Setter
	private int id;
	@Getter
	@Setter
	private String authRole;
	@Getter
	@Setter
	private String grantAuthRole;
}