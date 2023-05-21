package ru.kpfu.itis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.dto.request.PasswordChangeRequest;
import ru.kpfu.itis.dto.request.UserEditRequest;
import ru.kpfu.itis.dto.response.UserResponse;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.security.AccessToken;
import ru.kpfu.itis.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String getProfilePage(@AuthenticationPrincipal AccessToken accessToken, ModelMap modelMap) {
        Optional<UserResponse> optionalResponse = userService.getUserResponseById(accessToken.getId());
        UserResponse userResponse;
        userResponse = optionalResponse.orElseGet(() -> UserResponse.builder().build());
        modelMap.addAttribute("user", userResponse);
        return "profile";
    }

    @GetMapping("/edit")
    public String getEditPage(ModelMap modelMap) {
        modelMap.addAttribute("user", UserEditRequest.builder().build());
        return "profile_edit";
    }

    @PostMapping("/edit")
    public String editPage(@AuthenticationPrincipal AccessToken accessToken,
                           @Valid @ModelAttribute("user") UserEditRequest request,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile_edit";
        }

        Optional<User> optionalUser = userService.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            bindingResult.rejectValue("email", "", "Этот email уже занят на сайте");
            return "profile_edit";
        }
        userService.updateById(request, accessToken.getId());
        return "redirect:/profile";
    }

    @GetMapping("/change_password")
    public String getChangePasswordPage(ModelMap modelMap) {
        PasswordChangeRequest request = PasswordChangeRequest.builder().build();
        modelMap.addAttribute("changeRequest", request);
        return "change_password";
    }

    @PostMapping("/change_password")
    public String changePassword(@AuthenticationPrincipal AccessToken accessToken,
                                 @Valid @ModelAttribute("changeRequest") PasswordChangeRequest request,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "change_password";
        }
        Optional<User> optionalUser = userService.findByEmail(accessToken.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!passwordEncoder.matches(request.getOldPassword(), user.getHashPassword())) {
                bindingResult.rejectValue("oldPassword", "", "Неверный старый пароль");
                return "change_password";
            }
            userService.updateById(request, accessToken.getId());
            return "change_password_success";
        } else {
            return "something_went_wrong";
        }
    }

    @GetMapping("/adverts")
    public String getAdverts() {
        return "";
    }

    @GetMapping("/favorites")
    public String getFavorites() {
        return "";
    }
}
