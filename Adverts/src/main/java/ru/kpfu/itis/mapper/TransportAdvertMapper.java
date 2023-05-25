package ru.kpfu.itis.mapper;

import ru.kpfu.itis.dto.response.TransportAdvertResponse;
import ru.kpfu.itis.dto.response.TransportAdvertSearchResponse;
import ru.kpfu.itis.model.File;
import ru.kpfu.itis.model.TransportAdvert;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class TransportAdvertMapper {

    public static TransportAdvertSearchResponse getSearchResponse(TransportAdvert advert) {
        return TransportAdvertSearchResponse.builder()
                .id(advert.getId())
                .name(advert.getName())
                .price(advert.getPrice().toString())
                .build();
    }

    public static TransportAdvertResponse getResponse(TransportAdvert advert) {
        return TransportAdvertResponse.builder()
                .id(advert.getId())
                .fileNames(advert.getFiles().stream().map(File::getName).collect(Collectors.toList()))
                .firstName(advert.getUser().getFirstName())
                .lastName(advert.getUser().getLastName())
                .name(advert.getName())
                .brand(advert.getBrand())
                .isNew(advert.getIsNew())
                .isRegistered(advert.getIsRegistered())
                .locality(advert.getLocality())
                .description(advert.getDescription())
                .mileage(advert.getMileage())
                .price(advert.getPrice())
                .createdDate(advert.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .updatedDate(advert.getUpdatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .phone(advert.getPhone())
                .build();
    }
}
