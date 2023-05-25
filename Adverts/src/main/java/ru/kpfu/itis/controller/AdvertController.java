package ru.kpfu.itis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.dto.request.ElectronicsAdvertCreateRequest;
import ru.kpfu.itis.dto.request.TransportAdvertCreateRequest;
import ru.kpfu.itis.dto.response.ElectronicsAdvertResponse;
import ru.kpfu.itis.dto.response.TransportAdvertResponse;
import ru.kpfu.itis.exception.HttpNotFoundException;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.mapper.ElectronicsAdvertMapper;
import ru.kpfu.itis.mapper.TransportAdvertMapper;
import ru.kpfu.itis.model.ElectronicsAdvert;
import ru.kpfu.itis.model.TransportAdvert;
import ru.kpfu.itis.model.enums.AdvertType;
import ru.kpfu.itis.security.AccessToken;
import ru.kpfu.itis.service.ElectronicsAdvertService;
import ru.kpfu.itis.service.FileService;
import ru.kpfu.itis.service.TransportAdvertService;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/advert")
@RequiredArgsConstructor
@Slf4j
public class AdvertController {

    private final TransportAdvertService transportAdvertService;
    private final ElectronicsAdvertService electronicsAdvertService;
    private final FileService fileService;

    @GetMapping("/new")
    public String getNewAdvertPage() {
        return "new_advert";
    }

    @GetMapping("/new/transport")
    public String getNewTransportAdvertPage(ModelMap modelMap) {
        modelMap.addAttribute("advert", TransportAdvertCreateRequest.builder().build());
        return "new_transport_advert";
    }

    @PostMapping("/new/transport")
    public String createTransportAdvert(@AuthenticationPrincipal AccessToken accessToken,
                                        @Valid @ModelAttribute("advert") TransportAdvertCreateRequest request,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new_transport_advert";
        }
        try {
            UUID id = transportAdvertService.createByUserId(request, accessToken.getId());

            if (request.getFiles() != null) {
                TransportAdvert advert = transportAdvertService.findById(id).get();
                fileService.uploadAdvertFiles(request.getFiles(), AdvertType.TRANSPORT, advert);
            }
            return "redirect:/profile";
        } catch (UserNotFoundException e) {
            log.info("User with id: " + accessToken.getId() + " not found");
            return "something_went_wrong";
        }
    }

    @GetMapping("/new/electronics")
    public String getNewElectronicsAdvertPage(ModelMap modelMap) {
        modelMap.addAttribute("advert", TransportAdvertCreateRequest.builder().build());
        return "new_electronics_advert";
    }

    @PostMapping("/new/electronics")
    public String createElectronicsAdvert(@AuthenticationPrincipal AccessToken accessToken,
                                          @Valid @ModelAttribute("advert") ElectronicsAdvertCreateRequest request,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new_electronics_advert";
        }
        try {
            UUID id = electronicsAdvertService.createByUserId(request, accessToken.getId());

            if (request.getFiles() != null) {
                ElectronicsAdvert advert = electronicsAdvertService.findById(id).get();
                fileService.uploadAdvertFiles(request.getFiles(), AdvertType.ELECTRONICS, advert);
            }
            return "redirect:/profile";
        } catch (UserNotFoundException e) {
            log.info("User with id: " + accessToken.getId() + " not found");
            return "something_went_wrong";
        }
    }

    @GetMapping("/transport/{id}")
    public String getTransportAdvert(@PathVariable("id") UUID id, ModelMap modelMap) {
        Optional<TransportAdvert> optionalAdvert = transportAdvertService.findById(id);
        if (optionalAdvert.isPresent()) {
            TransportAdvert advert = optionalAdvert.get();
            TransportAdvertResponse response = TransportAdvertMapper.getResponse(advert);

            modelMap.addAttribute("advert", response);

            return "transport_advert";
        } else {
            throw new HttpNotFoundException();
        }
    }

    @GetMapping("/electronics/{id}")
    public String getElectronicsAdvert(@PathVariable("id") UUID id, ModelMap modelMap) {
        Optional<ElectronicsAdvert> optionalAdvert = electronicsAdvertService.findById(id);
        if (optionalAdvert.isPresent()) {
            ElectronicsAdvert advert = optionalAdvert.get();
            ElectronicsAdvertResponse response = ElectronicsAdvertMapper.getResponse(advert);

            modelMap.addAttribute("advert", response);

            return "electronics_advert";
        } else {
            throw new HttpNotFoundException();
        }
    }
}
