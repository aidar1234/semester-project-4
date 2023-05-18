package ru.kpfu.itis.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.security.AccessToken;
import ru.kpfu.itis.util.ModelAttributeGiver;

@Controller
@RequestMapping("/sign_in")
public class SignInController {
    //TODO отдельные таблицы под state и role, подтверждение почты, имя и фамилия на главной странице

    @GetMapping
    public String getSignInPage() {
        return "sign_in";
    }

    @PostMapping
    public String signIn(@AuthenticationPrincipal AccessToken accessToken, ModelMap modelMap) {
        return "redirect:/profile";
    }

    @PostMapping("/error")
    public String getSignInPage(ModelMap modelMap) {
        modelMap.addAttribute("error", ModelAttributeGiver.SIGN_IN_ERROR);
        return "sign_in";
    }
}
