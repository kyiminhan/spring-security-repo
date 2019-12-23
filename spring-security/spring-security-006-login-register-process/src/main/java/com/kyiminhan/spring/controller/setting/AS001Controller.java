package com.kyiminhan.spring.controller.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.kyiminhan.spring.service.AS001Service;
import com.kyiminhan.spring.service.dto.AS001RegisterationDto;

@Controller
public class AS001Controller {

	@Autowired
	private AS001Service as001Service;

	@GetMapping(value = { "/do-registration" })
	public String doRegistration(final Model model) {
		model.addAttribute("dto", AS001RegisterationDto.builder().build());
		return "setting/AS001-registration";
	}

	@PostMapping(value = { "/do-registration" })
	public String doRegistration(@ModelAttribute final AS001RegisterationDto dto, final BindingResult result,
	        final Model model) {
		this.as001Service.userAccountRegistration(dto);
		return "redirect:/do-registration";
	}

	@GetMapping(value = { "/confirm-registration" })
	public String confirmRegistration(final Model model) {

		return "setting/AS001-reg-confirmation";
	}
}
