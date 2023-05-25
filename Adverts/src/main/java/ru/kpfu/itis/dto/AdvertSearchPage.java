package ru.kpfu.itis.dto;

import lombok.Builder;
import lombok.Data;
import ru.kpfu.itis.dto.response.AdvertSearchResponse;

import java.util.List;

@Builder
@Data
public class AdvertSearchPage {

    private List<AdvertSearchResponse> adverts;
}
