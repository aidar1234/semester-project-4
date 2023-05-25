package ru.kpfu.itis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.annotation.Log;
import ru.kpfu.itis.dto.request.PasswordChangeRequest;
import ru.kpfu.itis.dto.request.UserEditRequest;
import ru.kpfu.itis.dto.response.*;
import ru.kpfu.itis.exception.AdvertAlreadyFavoriteException;
import ru.kpfu.itis.exception.AdvertNotFoundException;
import ru.kpfu.itis.exception.HttpNotFoundException;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.mapper.ElectronicsAdvertMapper;
import ru.kpfu.itis.mapper.TransportAdvertMapper;
import ru.kpfu.itis.model.ElectronicsAdvert;
import ru.kpfu.itis.model.TransportAdvert;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.security.AccessToken;
import ru.kpfu.itis.service.ElectronicsAdvertService;
import ru.kpfu.itis.service.FileService;
import ru.kpfu.itis.service.TransportAdvertService;
import ru.kpfu.itis.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final TransportAdvertService transportAdvertService;
    private final ElectronicsAdvertService electronicsAdvertService;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    @GetMapping
    public String getProfilePage(@AuthenticationPrincipal AccessToken accessToken, ModelMap modelMap) {
        Optional<UserResponse> optionalResponse = userService.getUserResponseById(accessToken.getId());
        UserResponse userResponse = optionalResponse.orElseGet(() -> UserResponse.builder().build());
        modelMap.addAttribute("user", userResponse);
        return "profile";
    }

    @GetMapping("/edit")
    public String getEditPage(ModelMap modelMap) {
        modelMap.addAttribute("user", UserEditRequest.builder().build());
        return "profile_edit";
    }

    @PostMapping("/edit")
    public String editProfile(@AuthenticationPrincipal AccessToken accessToken,
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
        User user = userService.findById(accessToken.getId()).get();
        fileService.uploadUserFile(request.getFile(), user);
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
    public String getAdverts(@AuthenticationPrincipal AccessToken accessToken, ModelMap modelMap) {
        User user = userService.findById(accessToken.getId()).get();
        if (user.getTransportAdverts() != null) {
            List<TransportAdvertSearchResponse> transport = user.getTransportAdverts().stream().map(advert -> TransportAdvertSearchResponse.builder()
                    .id(advert.getId())
                    .name(advert.getName())
                    .price(advert.getPrice().toString())
                    .build()).collect(Collectors.toList());

            modelMap.addAttribute("transportAdverts", transport);
        }

        if (user.getElectronicsAdverts() != null) {
            List<ElectronicsAdvertSearchResponse> electronics = user.getElectronicsAdverts().stream().map(advert -> ElectronicsAdvertSearchResponse.builder()
                    .id(advert.getId())
                    .name(advert.getName())
                    .price(advert.getPrice().toString())
                    .build()).collect(Collectors.toList());

            modelMap.addAttribute("electronicsAdverts", electronics);
        }

        return "my_adverts";
    }

    @GetMapping("/favorites")
    public String getFavorites(@AuthenticationPrincipal AccessToken accessToken, ModelMap modelMap) {
        User user = userService.findById(accessToken.getId()).get();
        if (user.getFavoriteTransportAdverts() != null) {
            List<TransportAdvertSearchResponse> transport = user.getFavoriteTransportAdverts().stream().map(advert -> TransportAdvertSearchResponse.builder()
                    .id(advert.getId())
                    .name(advert.getName())
                    .price(advert.getPrice().toString())
                    .build()).collect(Collectors.toList());

            modelMap.addAttribute("transportAdverts", transport);
        }

        if (user.getFavoriteElectronicsAdverts() != null) {
            List<ElectronicsAdvertSearchResponse> electronics = user.getFavoriteElectronicsAdverts().stream().map(advert -> ElectronicsAdvertSearchResponse.builder()
                    .id(advert.getId())
                    .name(advert.getName())
                    .price(advert.getPrice().toString())
                    .build()).collect(Collectors.toList());

            modelMap.addAttribute("electronicsAdverts", electronics);
        }
        return "my_favorites";
    }

    @Log
    @GetMapping("/phone")
    public ResponseEntity<String> getPhone(@AuthenticationPrincipal AccessToken accessToken) {
        Optional<UserResponse> optionalUserResponse = userService.getUserResponseById(accessToken.getId());
        return optionalUserResponse
                .map(userResponse -> ResponseEntity.ok(userResponse.getPhone()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Log
    @GetMapping("/locality")
    public ResponseEntity<String> getLocality(@AuthenticationPrincipal AccessToken accessToken) {
        Optional<UserResponse> optionalUserResponse = userService.getUserResponseById(accessToken.getId());
        return optionalUserResponse
                .map(userResponse -> ResponseEntity.ok(userResponse.getLocality()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Log
    @GetMapping("/favorite/transport/remove/{id}")
    public ResponseEntity<String> removeTransportFavorite(@PathVariable("id") UUID id, @AuthenticationPrincipal AccessToken accessToken) {
        try {
            userService.removeFavoriteTransportAdvertByUserIdAndAdvertId(accessToken.getId(), id);
        } catch (UserNotFoundException | AdvertNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Log
    @GetMapping("/favorite/transport/add/{id}")
    public ResponseEntity<String> addTransportFavorite(@PathVariable("id") UUID id, @AuthenticationPrincipal AccessToken accessToken) {
        try {
            userService.addFavoriteTransportAdvertByUserIdAndAdvertId(accessToken.getId(), id);
        } catch (UserNotFoundException | AdvertNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AdvertAlreadyFavoriteException e) {
            return ResponseEntity.status(406).build();
        }
        return ResponseEntity.ok().build();
    }

    @Log
    @GetMapping("/adverts/transport/remove/{id}")
    public ResponseEntity<String> removeTransportAdvert(@PathVariable("id") UUID id) {
        try {
            transportAdvertService.removeById(id);
        } catch (AdvertNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/advert/transport/{id}")
    public String getTransportAdvert(@PathVariable UUID id, ModelMap modelMap) {
        Optional<TransportAdvert> optionalAdvert = transportAdvertService.findById(id);
        if (optionalAdvert.isPresent()) {
            TransportAdvert advert = optionalAdvert.get();
            TransportAdvertResponse response = TransportAdvertMapper.getResponse(advert);

            modelMap.addAttribute("advert", response);

            return "my_transport_advert";
        } else {
            throw new HttpNotFoundException();
        }
    }

    @GetMapping("/favorite/transport/{id}")
    public String getFavoriteTransportAdvert(@PathVariable UUID id, ModelMap modelMap) {
        Optional<TransportAdvert> optionalAdvert = transportAdvertService.findById(id);
        if (optionalAdvert.isPresent()) {
            TransportAdvert advert = optionalAdvert.get();
            TransportAdvertResponse response = TransportAdvertMapper.getResponse(advert);

            modelMap.addAttribute("advert", response);

            return "my_transport_favorite_advert";
        } else {
            throw new HttpNotFoundException();
        }
    }

    @Log
    @GetMapping("/favorite/electronics/remove/{id}")
    public ResponseEntity<String> removeElectronicsFavorite(@PathVariable("id") UUID id, @AuthenticationPrincipal AccessToken accessToken) {
        try {
            userService.removeFavoriteElectronicsAdvertByUserIdAndAdvertId(accessToken.getId(), id);
        } catch (UserNotFoundException | AdvertNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Log
    @GetMapping("/favorite/electronics/add/{id}")
    public ResponseEntity<String> addElectronicsFavorite(@PathVariable("id") UUID id, @AuthenticationPrincipal AccessToken accessToken) {
        try {
            userService.addFavoriteElectronicsAdvertByUserIdAndAdvertId(accessToken.getId(), id);
        } catch (UserNotFoundException | AdvertNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AdvertAlreadyFavoriteException e) {
            return ResponseEntity.status(406).build();
        }
        return ResponseEntity.ok().build();
    }

    @Log
    @GetMapping("/adverts/electronics/remove/{id}")
    public ResponseEntity<String> removeElectronicsAdvert(@PathVariable("id") UUID id) {
        try {
            electronicsAdvertService.removeById(id);
        } catch (AdvertNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/advert/electronics/{id}")
    public String getElectronicsAdvert(@PathVariable UUID id, ModelMap modelMap) {
        Optional<ElectronicsAdvert> optionalAdvert = electronicsAdvertService.findById(id);
        if (optionalAdvert.isPresent()) {
            ElectronicsAdvert advert = optionalAdvert.get();
            ElectronicsAdvertResponse response = ElectronicsAdvertMapper.getResponse(advert);

            modelMap.addAttribute("advert", response);

            return "my_electronics_advert";
        } else {
            throw new HttpNotFoundException();
        }
    }

    @GetMapping("/favorite/electronics/{id}")
    public String getFavoriteElectronicsAdvert(@PathVariable UUID id, ModelMap modelMap) {
        Optional<ElectronicsAdvert> optionalAdvert = electronicsAdvertService.findById(id);
        if (optionalAdvert.isPresent()) {
            ElectronicsAdvert advert = optionalAdvert.get();
            ElectronicsAdvertResponse response = ElectronicsAdvertMapper.getResponse(advert);

            modelMap.addAttribute("advert", response);

            return "my_electronics_favorite_advert";
        } else {
            throw new HttpNotFoundException();
        }
    }
}
