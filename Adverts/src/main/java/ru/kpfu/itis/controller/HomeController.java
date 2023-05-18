package ru.kpfu.itis.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.security.AccessToken;

import static ru.kpfu.itis.util.ModelAttributeAdder.*;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping()
    public String getHomePage(@AuthenticationPrincipal AccessToken accessToken, ModelMap modelMap) {
        addFirstNameAndLastName(accessToken, modelMap);
        return "home";
    }
}
