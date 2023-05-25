package ru.kpfu.itis.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class ElectronicsAdvertSearchResponse {

    private String name;

    private UUID id;

    private String price;
}
