package ru.kpfu.itis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransportAdvertResponse {

    private UUID id;

    private List<String> fileNames;

    private String firstName;

    private String lastName;

    private String name;

    private String phone;

    private Float price;

    private String locality;

    private String brand;

    private Boolean isNew;

    private Boolean isRegistered;

    private Integer mileage;

    private String description;

    private String createdDate;

    private String updatedDate;

}
