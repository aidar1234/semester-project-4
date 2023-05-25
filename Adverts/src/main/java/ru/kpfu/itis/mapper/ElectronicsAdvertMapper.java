package ru.kpfu.itis.mapper;

import ru.kpfu.itis.dto.response.ElectronicsAdvertResponse;
import ru.kpfu.itis.dto.response.ElectronicsAdvertSearchResponse;
import ru.kpfu.itis.model.ElectronicsAdvert;
import ru.kpfu.itis.model.File;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class ElectronicsAdvertMapper {

    public static ElectronicsAdvertSearchResponse getSearchResponse(ElectronicsAdvert advert) {
        return ElectronicsAdvertSearchResponse.builder()
                .id(advert.getId())
                .name(advert.getName())
                .price(advert.getPrice().toString())
                .build();
    }

    public static ElectronicsAdvertResponse getResponse(ElectronicsAdvert advert) {
        return ElectronicsAdvertResponse.builder()
                .id(advert.getId())
                .fileNames(advert.getFiles().stream().map(File::getName).collect(Collectors.toList()))
                .firstName(advert.getUser().getFirstName())
                .lastName(advert.getUser().getLastName())
                .name(advert.getName())
                .locality(advert.getLocality())
                .description(advert.getDescription())
                .price(advert.getPrice())
                .createdDate(advert.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .updatedDate(advert.getUpdatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .phone(advert.getPhone())
                .build();
    }
}
