package ru.kpfu.itis.dto;

import lombok.Builder;
import lombok.Data;
import ru.kpfu.itis.dto.response.ElectronicsAdvertSearchResponse;

import java.util.List;

@Builder
@Data
public class ElectronicsAdvertSearchPage {

    public static final Integer pageSize = 10;

    private List<ElectronicsAdvertSearchResponse> adverts;

    private String date;

    private String price;

    private String locality;

    private Boolean forward;

    private Boolean back;

    private Integer currentPageNumber;
}
