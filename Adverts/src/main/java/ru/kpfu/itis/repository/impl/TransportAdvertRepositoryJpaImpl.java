package ru.kpfu.itis.repository.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.exception.AdvertNotFoundException;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.model.TransportAdvert;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.model.enums.TransportKind;
import ru.kpfu.itis.repository.TransportAdvertRepository;
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
public class TransportAdvertRepositoryJpaImpl implements TransportAdvertRepository {

    private final UserRepository userRepository;

    @PersistenceContext
    private Session entityManager;

    @Override
    public List<TransportAdvert> findAll(Pageable pageable, List<Map.Entry<String, String>> valueFields, List<Map.Entry<String, Sort.Direction>> directionFields) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransportAdvert> criteriaQuery = criteriaBuilder.createQuery(TransportAdvert.class);
        Root<TransportAdvert> root = criteriaQuery.from(TransportAdvert.class);
        criteriaQuery.select(root);

        String kind = null;
        String locality = null;
        if (valueFields != null) {
            for (Map.Entry<String, String> valueField : valueFields) {
                if (valueField.getKey().equals("kind")) {
                    kind = valueField.getValue();
                }
                if (valueField.getKey().equals("locality")) {
                    locality = valueField.getValue();
                }
            }
            if (locality == null) {
                criteriaQuery.where(criteriaBuilder.equal(root.get("kind"), TransportKind.valueOf(kind)));
            } else {
                criteriaQuery.where(criteriaBuilder.equal(root.get("kind"), TransportKind.valueOf(kind)), criteriaBuilder.equal(root.get("locality"), locality));
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

        TypedQuery<TransportAdvert> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    @Override
    public Optional<List<TransportAdvert>> findByName(String name) {
        TypedQuery<TransportAdvert> typedQuery = entityManager.createQuery("SELECT advert FROM TransportAdvert advert WHERE advert.name = :name", TransportAdvert.class);
        typedQuery.setParameter("name", name);
        try {
            List<TransportAdvert> list = typedQuery.getResultList();
            return Optional.of(list);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public void removeById(UUID id) throws AdvertNotFoundException {
        try {
            TransportAdvert advert = entityManager.get(TransportAdvert.class, id);
            entityManager.remove(advert);
        } catch (NullPointerException e) {
            throw new AdvertNotFoundException();
        }
    }

    @Transactional
    @Override
    public UUID saveByUserId(TransportAdvert advert, UUID id) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            advert.setUser(user);
            return (UUID) entityManager.save(advert);
        }
        throw new UserNotFoundException();
    }

    @Override
    public Optional<TransportAdvert> findById(UUID id) {
        try {
            TransportAdvert advert = entityManager.get(TransportAdvert.class, id);
            return Optional.of(advert);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}
