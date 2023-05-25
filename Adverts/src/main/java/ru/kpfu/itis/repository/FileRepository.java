package ru.kpfu.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.model.File;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    @Query("FROM File file WHERE file.hash = :hash AND file.size = :size")
    Optional<File> findByHashAndSize(@Param("hash") String hash, @Param("size") Long size);
}
