package com.kyiminhan.spring.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
public class Mail implements Serializable {

	private static final long serialVersionUID = 1L;

	private String from;
	private String to;
	private String subject;
	private List<Object> attachments;
	private Map<String, Object> model;

}