package ru.kpfu.itis.util;

import org.springframework.ui.ModelMap;
import ru.kpfu.itis.security.AccessToken;

public class ModelAttributeAdder {

    public static void addFirstNameAndLastName(AccessToken accessToken, ModelMap modelMap) {
        if (accessToken != null) {
            modelMap.addAttribute("firstNameLastName",
                    accessToken.getFirstName() + " " + accessToken.getLastName()
            );
        }
    }
}
