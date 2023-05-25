package ru.kpfu.itis.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class AdvertSearchResponse {

    private String name;

    private String price;

    private String type;

    private UUID id;
}
