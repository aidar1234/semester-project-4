package ru.kpfu.itis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sign_in")
public class SignInController {

    @GetMapping
    public String getSignInPage() {
        return "sign_in";
    }

    @PostMapping
    public String signIn() {
        // handling in JwtUsernamePasswordAuthenticationFilter
        return "redirect:/profile";
    }

    @PostMapping("/error")
    public String getSignInPage(ModelMap modelMap) {
        modelMap.addAttribute("error", "Неверный email или пароль");
        return "sign_in";
    }
}
