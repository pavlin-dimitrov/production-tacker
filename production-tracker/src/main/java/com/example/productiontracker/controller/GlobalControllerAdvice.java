package com.example.productiontracker.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Calendar;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isAdmin", isAdmin(authentication));
        }
    }

    @ModelAttribute
    public void addWeekNumberToModel(Model model) {
        Calendar calendar = Calendar.getInstance();
        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        model.addAttribute("weekNumber", weekNumber);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }
}

