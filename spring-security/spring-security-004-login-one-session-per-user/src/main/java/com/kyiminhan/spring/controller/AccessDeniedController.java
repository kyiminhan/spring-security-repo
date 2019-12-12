package com.kyiminhan.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

	@GetMapping(value = { "/access-denied" })
	public String accessDenied(final Model model) {
		return "access-denied";
	}
}