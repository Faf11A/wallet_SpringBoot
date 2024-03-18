package com.springBootWallet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginController {

	@GetMapping("/login")
	public String showLoginForm(Model model, @RequestParam(value = "mode", defaultValue = "login") String mode) {
		model.addAttribute("mode", mode);
		return "login";
	}
}