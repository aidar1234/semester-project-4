package ru.kpfu.itis.service;

import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.model.enums.AdvertType;

public interface FileService {

    void uploadAdvertFiles(MultipartFile[] files, AdvertType advertType, Object advert);

    void uploadUserFile(MultipartFile file, User user);
}
