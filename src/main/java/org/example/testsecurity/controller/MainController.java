package org.example.testsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(Model model) {
        System.out.println("MainController.mainPage");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String authority = authorities.iterator().next().getAuthority();

        model.addAttribute("userId", userId);
        model.addAttribute("authority", authority);
        System.out.println("model = " + model);
        return "main";
    }
}
