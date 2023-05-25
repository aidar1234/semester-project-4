package ru.kpfu.itis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.dto.request.TransportAdvertCreateRequest;
import ru.kpfu.itis.dto.response.TransportAdvertResponse;
import ru.kpfu.itis.dto.response.TransportAdvertSearchResponse;
import ru.kpfu.itis.exception.AdvertNotFoundException;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.mapper.ElectronicsAdvertMapper;
import ru.kpfu.itis.mapper.TransportAdvertMapper;
import ru.kpfu.itis.model.TransportAdvert;
import ru.kpfu.itis.model.enums.TransportKind;
import ru.kpfu.itis.repository.TransportAdvertRepository;
import ru.kpfu.itis.service.TransportAdvertService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransportAdvertServiceImpl implements TransportAdvertService {

    private final TransportAdvertRepository transportAdvertRepository;

    @Override
    public Page<TransportAdvertSearchResponse> findAll(Pageable pageable, List<Map.Entry<String, String>> valueFields, List<Map.Entry<String, Sort.Direction>> directionFields) {
        List<TransportAdvert> all = transportAdvertRepository.findAll(pageable, valueFields, directionFields);
        List<TransportAdvertSearchResponse> page = all.stream().map(TransportAdvertMapper::getSearchResponse).collect(Collectors.toList());
        return new PageImpl<>(page);
    }

    @Override
    public UUID createByUserId(TransportAdvertCreateRequest request, UUID id) throws UserNotFoundException {

        TransportAdvert advert = TransportAdvert.builder()
                .name(request.getName())
                .price(Float.parseFloat(request.getPrice()))
                .brand(request.getBrand())
                .phone(request.getPhone())
                .description(request.getDescription())
                .isNew(request.getIsNew() != null && request.getIsNew().equals("on"))
                .isRegistered(request.getIsRegistered() != null && request.getIsRegistered().equals("on"))
                .kind(TransportKind.valueOf(request.getKind()))
                .mileage(request.getMileage())
                .locality(request.getLocality())
                .build();

        return transportAdvertRepository.saveByUserId(advert, id);
    }

    @Override
    public Optional<TransportAdvert> findById(UUID id) {
        return transportAdvertRepository.findById(id);
    }

    @Override
    public Optional<List<TransportAdvertResponse>> findByName(String name) {
        return transportAdvertRepository.findByName(name)
                .map(transportAdverts -> transportAdverts.stream()
                        .map(TransportAdvertMapper::getResponse)
                        .collect(Collectors.toList())
                );
    }

    @Override
    public void removeById(UUID id) throws AdvertNotFoundException {
        transportAdvertRepository.removeById(id);
    }
}
