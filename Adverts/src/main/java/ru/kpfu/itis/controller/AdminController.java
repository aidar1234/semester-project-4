package ru.kpfu.itis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.annotation.Log;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.service.UserService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @Log
    @GetMapping
    public String getAdminPage() {
        return "admin";
    }

    @PostMapping("/ban")
    public String banUserByEmail(@RequestParam("email") String email, ModelMap modelMap) {
        try {
            userService.banByEmail(email);
            modelMap.addAttribute("successBan", "Пользователь был забанен");
        } catch (UserNotFoundException e) {
            modelMap.addAttribute("errorBan", "Пользователя с таким email не существует");
        }
        return "admin";
    }

    @PostMapping("/delete")
    public String deleteUserByEmail(@RequestParam("email") String email, ModelMap modelMap) {
        try {
            userService.deleteByEmail(email);
            modelMap.addAttribute("successDelete", "Пользователь был удалён");
        } catch (UserNotFoundException e) {
            modelMap.addAttribute("errorDelete", "Пользователя с таким email не существует");
        }
        return "admin";
    }
}
