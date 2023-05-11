package ru.kpfu.itis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.util.ModelAttributeGiver;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/sign_in")
public class SignInController {

    @GetMapping
    public String getSignInPage() {
        return "sign_in";
    }

    @PostMapping
    public String signIn(ModelMap modelMap, HttpServletResponse response) {
        //TODO: add to response cookie
        return "profile";
    }

    @PostMapping("/error")
    public String getSignInPage(ModelMap modelMap) {
        modelMap.addAttribute("error", ModelAttributeGiver.SIGN_IN_ERROR);
        return "sign_in";
    }
}
