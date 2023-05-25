package ru.kpfu.itis.dto;

import lombok.Builder;
import lombok.Data;
import ru.kpfu.itis.dto.response.TransportAdvertSearchResponse;

import java.util.List;

@Builder
@Data
public class TransportAdvertsSearchPage {

    public static final Integer pageSize = 10;

    private List<TransportAdvertSearchResponse> adverts;

    private String date;

    private String price;

    private String kind;

    private String locality;

    private Boolean forward;

    private Boolean back;

    private Integer currentPageNumber;
}
