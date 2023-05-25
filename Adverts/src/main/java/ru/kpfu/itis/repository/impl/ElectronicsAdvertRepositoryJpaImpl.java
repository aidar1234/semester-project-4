package ru.kpfu.itis.repository.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.exception.AdvertNotFoundException;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.model.ElectronicsAdvert;
import ru.kpfu.itis.model.TransportAdvert;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.model.enums.TransportKind;
import ru.kpfu.itis.repository.ElectronicsAdvertRepository;
import ru.kpfu.itis.repository.UserRepository;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ElectronicsAdvertRepositoryJpaImpl implements ElectronicsAdvertRepository {

    private final UserRepository userRepository;

    @PersistenceContext
    private Session entityManager;

    @Override
    public List<ElectronicsAdvert> findAll(Pageable pageable, List<Map.Entry<String, String>> valueFields, List<Map.Entry<String, Sort.Direction>> directionFields) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ElectronicsAdvert> criteriaQuery = criteriaBuilder.createQuery(ElectronicsAdvert.class);
        Root<ElectronicsAdvert> root = criteriaQuery.from(ElectronicsAdvert.class);
        criteriaQuery.select(root);

        String locality = null;
        if (valueFields != null) {
            for (Map.Entry<String, String> valueField : valueFields) {
                if (valueField.getKey().equals("locality")) {
                    locality = valueField.getValue();
                }
            }
            if (locality != null) {
                criteriaQuery.where(criteriaBuilder.equal(root.get("locality"), locality));
            }
        }

        if (directionFields != null)
            for (Map.Entry<String, Sort.Direction> directionField : directionFields) {
                if (directionField.getValue().isAscending()) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(directionField.getKey())));
                } else if (directionField.getValue().isDescending()) {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(directionField.getKey())));
                }
            }

        TypedQuery<ElectronicsAdvert> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Override
    public Optional<List<ElectronicsAdvert>> findByName(String name) {
        TypedQuery<ElectronicsAdvert> typedQuery = entityManager.createQuery("SELECT advert FROM ElectronicsAdvert advert WHERE advert.name = :name", ElectronicsAdvert.class);
        typedQuery.setParameter("name", name);
        try {
            List<ElectronicsAdvert> list = typedQuery.getResultList();
            return Optional.of(list);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public void removeById(UUID id) throws AdvertNotFoundException {
        try {
            ElectronicsAdvert advert = entityManager.get(ElectronicsAdvert.class, id);
            entityManager.remove(advert);
        } catch (NullPointerException e) {
            throw new AdvertNotFoundException();
        }
    }

    @Transactional
    @Override
    public UUID saveByUserId(ElectronicsAdvert advert, UUID id) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            advert.setUser(user);
            UUID uuid = (UUID) entityManager.save(advert);
            entityManager.flush();
            return uuid;
        }
        throw new UserNotFoundException();
    }

    @Override
    public Optional<ElectronicsAdvert> findById(UUID id) {
        try {
            ElectronicsAdvert advert = entityManager.get(ElectronicsAdvert.class, id);
            return Optional.of(advert);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}
