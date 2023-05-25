package ru.kpfu.itis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.dto.request.ElectronicsAdvertCreateRequest;
import ru.kpfu.itis.dto.response.ElectronicsAdvertResponse;
import ru.kpfu.itis.dto.response.ElectronicsAdvertSearchResponse;
import ru.kpfu.itis.dto.response.TransportAdvertResponse;
import ru.kpfu.itis.exception.AdvertNotFoundException;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.mapper.ElectronicsAdvertMapper;
import ru.kpfu.itis.mapper.TransportAdvertMapper;
import ru.kpfu.itis.model.ElectronicsAdvert;
import ru.kpfu.itis.repository.ElectronicsAdvertRepository;
import ru.kpfu.itis.service.ElectronicsAdvertService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElectronicsAdvertServiceImpl implements ElectronicsAdvertService {

    private final ElectronicsAdvertRepository electronicsAdvertRepository;

    @Override
    public Page<ElectronicsAdvertSearchResponse> findAll(Pageable pageable, List<Map.Entry<String, String>> valueFields, List<Map.Entry<String, Sort.Direction>> directionFields) {
        List<ElectronicsAdvert> all = electronicsAdvertRepository.findAll(pageable, valueFields, directionFields);
        List<ElectronicsAdvertSearchResponse> page = all.stream().map(ElectronicsAdvertMapper::getSearchResponse).collect(Collectors.toList());
        return new PageImpl<>(page);
    }

    @Override
    public UUID createByUserId(ElectronicsAdvertCreateRequest request, UUID id) throws UserNotFoundException {
        ElectronicsAdvert advert = ElectronicsAdvert.builder()
                .name(request.getName())
                .price(Float.parseFloat(request.getPrice()))
                .phone(request.getPhone())
                .description(request.getDescription())
                .locality(request.getLocality())
                .build();

        return electronicsAdvertRepository.saveByUserId(advert, id);
    }

    @Override
    public Optional<ElectronicsAdvert> findById(UUID id) {
        return electronicsAdvertRepository.findById(id);
    }

    @Override
    public Optional<List<ElectronicsAdvertResponse>> findByName(String name) {
        return electronicsAdvertRepository.findByName(name)
                .map(electronicsAdverts -> electronicsAdverts.stream()
                        .map(ElectronicsAdvertMapper::getResponse)
                        .collect(Collectors.toList())
                );
    }

    @Override
    public void removeById(UUID id) throws AdvertNotFoundException {
        electronicsAdvertRepository.removeById(id);
    }
}
