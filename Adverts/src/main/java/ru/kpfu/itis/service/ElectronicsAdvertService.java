package ru.kpfu.itis.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.kpfu.itis.dto.request.ElectronicsAdvertCreateRequest;
import ru.kpfu.itis.dto.response.ElectronicsAdvertResponse;
import ru.kpfu.itis.dto.response.ElectronicsAdvertSearchResponse;
import ru.kpfu.itis.dto.response.TransportAdvertResponse;
import ru.kpfu.itis.exception.AdvertNotFoundException;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.model.ElectronicsAdvert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ElectronicsAdvertService {

    Page<ElectronicsAdvertSearchResponse> findAll(Pageable pageable, List<Map.Entry<String, String>> valueFields, List<Map.Entry<String, Sort.Direction>> directionFields);

    UUID createByUserId(ElectronicsAdvertCreateRequest request, UUID id) throws UserNotFoundException;

    Optional<ElectronicsAdvert> findById(UUID id);

    Optional<List<ElectronicsAdvertResponse>> findByName(String name);

    void removeById(UUID id) throws AdvertNotFoundException;
}
