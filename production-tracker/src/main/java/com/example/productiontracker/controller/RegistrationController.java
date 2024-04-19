package com.example.productiontracker.controller;

import com.example.productiontracker.dto.RegistrationDto;
import com.example.productiontracker.enums.Role;
import com.example.productiontracker.service.contract.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public String registrationForm(Model model) {
        model.addAttribute("account", new RegistrationDto());
        model.addAttribute("roles", Role.values()); // Providing roles for the dropdown
        return "register";
    }

    @PostMapping
    public String registerAccount(@ModelAttribute("account") RegistrationDto registrationDto, RedirectAttributes attributes) {
        try {
            accountService.registerNewAccount(registrationDto);
            attributes.addFlashAttribute("success", "Registration successful. Please log in.");
            return "redirect:/home";
        } catch (RuntimeException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
