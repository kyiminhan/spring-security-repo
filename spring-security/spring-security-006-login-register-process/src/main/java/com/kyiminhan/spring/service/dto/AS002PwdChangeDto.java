package com.kyiminhan.spring.service.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AS002PwdChangeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;
	private String newPassword;
	private String confirmNewPassword;
}