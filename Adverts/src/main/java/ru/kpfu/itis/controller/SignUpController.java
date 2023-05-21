package ru.kpfu.itis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.dto.request.UserSignUpRequest;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/sign_up")
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    @GetMapping
    public String getSignUpPage(ModelMap modelMap) {
        modelMap.addAttribute("user", UserSignUpRequest.builder().build());
        return "sign_up";
    }

    @PostMapping
    public String signUp(@Valid @ModelAttribute(name = "user") UserSignUpRequest request,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "sign_up";
        }

        Optional<User> optionalUser = userService.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            bindingResult.rejectValue("email", "", "Этот email уже занят на сайте");
            return "sign_up";
        }
        userService.signUp(request);
        return "sign_up_success";
    }
}
