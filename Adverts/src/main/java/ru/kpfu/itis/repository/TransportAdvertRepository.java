package ru.kpfu.itis.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.kpfu.itis.exception.AdvertNotFoundException;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.model.TransportAdvert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface TransportAdvertRepository {

    List<TransportAdvert> findAll(Pageable pageable,List<Map.Entry<String, String>> valueFields, List<Map.Entry<String, Sort.Direction>> directionFields);

    Optional<List<TransportAdvert>> findByName(String name);

    void removeById(UUID id) throws AdvertNotFoundException;

    UUID saveByUserId(TransportAdvert advert, UUID id) throws UserNotFoundException;

    Optional<TransportAdvert> findById(UUID id);
}
