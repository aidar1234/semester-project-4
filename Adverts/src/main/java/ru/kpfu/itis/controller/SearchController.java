package ru.kpfu.itis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.dto.AdvertSearchPage;
import ru.kpfu.itis.dto.ElectronicsAdvertSearchPage;
import ru.kpfu.itis.dto.TransportAdvertsSearchPage;
import ru.kpfu.itis.dto.response.*;
import ru.kpfu.itis.service.ElectronicsAdvertService;
import ru.kpfu.itis.service.TransportAdvertService;

import java.util.*;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final TransportAdvertService transportAdvertService;
    private final ElectronicsAdvertService electronicsAdvertService;

    @GetMapping()
    public String getSearchPage(@RequestParam String request, ModelMap modelMap) {
        List<AdvertSearchResponse> list = new ArrayList<>();
        Optional<List<TransportAdvertResponse>> transportList = transportAdvertService.findByName(request);
        if (transportList.isPresent()) {
            for (TransportAdvertResponse response : transportList.get()) {
                list.add(AdvertSearchResponse.builder().id(response.getId()).type("transport").price(String.valueOf(response.getPrice())).name(response.getName()).build());
            }
        }

        Optional<List<ElectronicsAdvertResponse>> electronicsList = electronicsAdvertService.findByName(request);
        if (electronicsList.isPresent()) {
            for (ElectronicsAdvertResponse response : electronicsList.get()) {
                list.add(AdvertSearchResponse.builder().id(response.getId()).type("electronics").price(String.valueOf(response.getPrice())).name(response.getName()).build());
            }
        }
        modelMap.addAttribute("page", AdvertSearchPage.builder().adverts(list).build());
        return "search_adverts";
    }

    @GetMapping("/transport")
    public String getTransportSearchPage(@ModelAttribute("page") TransportAdvertsSearchPage request, ModelMap modelMap) {

        if (request.getCurrentPageNumber() == null) {
            getTransportSearchPage(modelMap);
            return "transport_adverts_search";
        }

        Integer currentPageNumber = request.getCurrentPageNumber();
        if (Boolean.TRUE.equals(request.getForward())) {
            request.setCurrentPageNumber(++currentPageNumber);
        }
        if (Boolean.TRUE.equals(request.getBack()) && currentPageNumber != 1) {
            request.setCurrentPageNumber(--currentPageNumber);
        }
        if (!Boolean.TRUE.equals(request.getForward()) && !Boolean.TRUE.equals(request.getBack())) {
            request.setCurrentPageNumber(1);
        }
        String locality = request.getLocality();
        String price = request.getPrice();
        String date = request.getDate();
        String kind = request.getKind();

        List<Map.Entry<String, String>> valueFields = new ArrayList<>();

        if (locality != null && !locality.isEmpty())
            valueFields.add(new AbstractMap.SimpleEntry<>("locality", locality));

        if ("Автомобиль".equals(kind))
            valueFields.add(new AbstractMap.SimpleEntry<>("kind", "AUTOMOBILE"));
        else if ("Мотоцикл".equals(kind))
            valueFields.add(new AbstractMap.SimpleEntry<>("kind", "MOTORBIKE"));
        else if ("Грузовик".equals(kind))
            valueFields.add(new AbstractMap.SimpleEntry<>("kind", "TRUCK"));

        List<Map.Entry<String, Sort.Direction>> directionFields = new ArrayList<>();

        if ("Новые".equals(date))
            directionFields.add(new AbstractMap.SimpleEntry<>("createdDate", Sort.Direction.ASC));
        else if ("Старые".equals(date))
            directionFields.add(new AbstractMap.SimpleEntry<>("createdDate", Sort.Direction.DESC));

        if ("Дешевые".equals(price))
            directionFields.add(new AbstractMap.SimpleEntry<>("price", Sort.Direction.ASC));
        else if ("Дорогие".equals(price))
            directionFields.add(new AbstractMap.SimpleEntry<>("price", Sort.Direction.DESC));

        Page<TransportAdvertSearchResponse> page = transportAdvertService.findAll(PageRequest.of(currentPageNumber, TransportAdvertsSearchPage.pageSize), valueFields, directionFields);
        request.setAdverts(page.getContent());
        request.setBack(false);
        request.setForward(false);
        return "transport_adverts_search";
    }

    @GetMapping("/electronics")
    public String getSearchPage(@ModelAttribute("page") ElectronicsAdvertSearchPage request, ModelMap modelMap) {

        if (request.getCurrentPageNumber() == null) {
            getElectronicsSearchPage(modelMap);
            return "electronics_adverts_search";
        }

        Integer currentPageNumber = request.getCurrentPageNumber();
        if (Boolean.TRUE.equals(request.getForward())) {
            request.setCurrentPageNumber(++currentPageNumber);
        }
        if (Boolean.TRUE.equals(request.getBack()) && currentPageNumber != 1) {
            request.setCurrentPageNumber(--currentPageNumber);
        }
        if (!Boolean.TRUE.equals(request.getForward()) && !Boolean.TRUE.equals(request.getBack())) {
            request.setCurrentPageNumber(1);
        }
        String locality = request.getLocality();
        String price = request.getPrice();
        String date = request.getDate();

        List<Map.Entry<String, String>> valueFields = new ArrayList<>();

        if (locality != null && !locality.isEmpty())
            valueFields.add(new AbstractMap.SimpleEntry<>("locality", locality));

        List<Map.Entry<String, Sort.Direction>> directionFields = new ArrayList<>();

        if ("Новые".equals(date))
            directionFields.add(new AbstractMap.SimpleEntry<>("createdDate", Sort.Direction.ASC));
        else if ("Старые".equals(date))
            directionFields.add(new AbstractMap.SimpleEntry<>("createdDate", Sort.Direction.DESC));

        if ("Дешевые".equals(price))
            directionFields.add(new AbstractMap.SimpleEntry<>("price", Sort.Direction.ASC));
        else if ("Дорогие".equals(price))
            directionFields.add(new AbstractMap.SimpleEntry<>("price", Sort.Direction.DESC));

        Page<ElectronicsAdvertSearchResponse> page = electronicsAdvertService.findAll(PageRequest.of(currentPageNumber, ElectronicsAdvertSearchPage.pageSize), valueFields, directionFields);
        request.setAdverts(page.getContent());
        request.setBack(false);
        request.setForward(false);
        return "electronics_adverts_search";
    }

    private void getTransportSearchPage(ModelMap modelMap) {

        List<Map.Entry<String, String>> valueFields = new ArrayList<>();
        valueFields.add(new AbstractMap.SimpleEntry<>("kind", "AUTOMOBILE"));

        List<Map.Entry<String, Sort.Direction>> directionFields = new ArrayList<>();
        directionFields.add(new AbstractMap.SimpleEntry<>("createdDate", Sort.Direction.ASC));
        directionFields.add(new AbstractMap.SimpleEntry<>("price", Sort.Direction.ASC));

        Page<TransportAdvertSearchResponse> page = transportAdvertService.findAll(PageRequest.of(1, TransportAdvertsSearchPage.pageSize), valueFields, directionFields);

        TransportAdvertsSearchPage advertsPage = TransportAdvertsSearchPage.builder()
                .adverts(page.getContent())
                .currentPageNumber(1)
                .build();

        modelMap.addAttribute("page", advertsPage);
    }

    private void getElectronicsSearchPage(ModelMap modelMap) {

        List<Map.Entry<String, Sort.Direction>> directionFields = new ArrayList<>();
        directionFields.add(new AbstractMap.SimpleEntry<>("createdDate", Sort.Direction.ASC));
        directionFields.add(new AbstractMap.SimpleEntry<>("price", Sort.Direction.ASC));

        Page<ElectronicsAdvertSearchResponse> page = electronicsAdvertService.findAll(PageRequest.of(1, ElectronicsAdvertSearchPage.pageSize), null, directionFields);

        ElectronicsAdvertSearchPage advertsPage = ElectronicsAdvertSearchPage.builder()
                .adverts(page.getContent())
                .currentPageNumber(1)
                .build();

        modelMap.addAttribute("page", advertsPage);
    }
}
