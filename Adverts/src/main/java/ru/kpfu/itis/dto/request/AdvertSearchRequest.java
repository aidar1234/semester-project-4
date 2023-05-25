package ru.kpfu.itis.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AdvertSearchRequest {

    private String name;
}
