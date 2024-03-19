package com.wallet_SpringBoot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    //show login page
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginForm(Model model, @RequestParam(value = "mode", defaultValue = "login") String mode) {
        model.addAttribute("mode", mode);
        return "login";
    }
}
