package ru.kpfu.itis.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.kpfu.itis.dto.request.TransportAdvertCreateRequest;
import ru.kpfu.itis.dto.response.TransportAdvertResponse;
import ru.kpfu.itis.dto.response.TransportAdvertSearchResponse;
import ru.kpfu.itis.exception.AdvertNotFoundException;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.model.TransportAdvert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface TransportAdvertService {

    Page<TransportAdvertSearchResponse> findAll(Pageable pageable, List<Map.Entry<String, String>> valueFields, List<Map.Entry<String, Sort.Direction>> directionFields);

    UUID createByUserId(TransportAdvertCreateRequest request, UUID id) throws UserNotFoundException;

    Optional<TransportAdvert> findById(UUID id);

    Optional<List<TransportAdvertResponse>> findByName(String name);

    void removeById(UUID id) throws AdvertNotFoundException;
}
