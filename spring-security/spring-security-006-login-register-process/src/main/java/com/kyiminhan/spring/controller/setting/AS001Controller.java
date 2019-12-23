package com.kyiminhan.spring.controller.setting;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AS001Controller {

	@GetMapping(value = { "/do-registration" })
	public String doRegistration(final Model model) {

		return "setting/AS001-registeration";
	}

	@PostMapping(value = { "/do-registration" })
	public String doRegistration(@ModelAttribute final Object obj, final BindingResult result, final Model model) {

		return "setting/AS001-registration";
	}

	@GetMapping(value = { "/confirm-registration" })
	public String confirmRegistration(final Model model) {

		return "setting/AS001-reg-confirmation";
	}
}
