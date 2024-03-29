package com.kyiminhan.spring.controller.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kyiminhan.spring.service.AS001Service;
import com.kyiminhan.spring.service.dto.AS001RegistrationDto;
import com.kyiminhan.spring.validator.AS001Validator;

@Controller
public class AS001Controller {

	@Autowired
	private AS001Service as001Service;

	@Autowired
	private AS001Validator validator;

	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.addValidators(this.validator);
	}

	@GetMapping(value = { "/do-registration" })
	public String doRegistration(final Model model) {
		model.addAttribute("dto", AS001RegistrationDto.builder().build());
		return "setting/AS001-registration";
	}

	@PostMapping(value = { "/do-registration" })
	public String doRegistration(@ModelAttribute("dto") @Validated final AS001RegistrationDto dto,
			final BindingResult result, final RedirectAttributes attributes) throws Exception {

		if (result.hasErrors()) {
			return "setting/AS001-registration";
		}
		this.as001Service.userAccountRegistration(dto);
		attributes.getFlashAttributes().clear();
		attributes.addFlashAttribute("messages", "success.register");
		return "redirect:/do-registration";
	}

	@GetMapping(value = { "/confirm-registration/{uuid}" })
	public String confirmRegistration(@PathVariable("uuid") final String uuid, final Model model) {
		if (this.as001Service.isRegistrationExpired(uuid)) {
			this.as001Service.deleteRegistrationExpired(uuid);
			model.addAttribute("errors", "error.register.confirmation");
		} else {
			this.as001Service.userAccountConfirmation(uuid);
			model.addAttribute("messages", "success.register.confirmation");
		}
		return "setting/AS001-reg-confirmation";
	}
}