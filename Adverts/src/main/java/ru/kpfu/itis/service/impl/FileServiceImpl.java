package ru.kpfu.itis.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.model.ElectronicsAdvert;
import ru.kpfu.itis.model.File;
import ru.kpfu.itis.model.TransportAdvert;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.model.enums.AdvertType;
import ru.kpfu.itis.repository.FileRepository;
import ru.kpfu.itis.service.FileService;
import ru.kpfu.itis.service.UserService;

import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserService userService;
    private final Environment environment;

    @Override
    public void uploadAdvertFiles(MultipartFile[] files, AdvertType advertType, Object advert) {
        if (files == null || files.length == 0) {
            return;
        }
        for (MultipartFile file : files) {
            if (file.getSize() == 0)
                continue;
            try {
                String hash = DigestUtils.md5Hex(file.getBytes());

                Optional<File> optionalFile = fileRepository.findByHashAndSize(hash, file.getSize());
                if (optionalFile.isPresent()) {
                    File f = optionalFile.get();
                    if (advertType == AdvertType.TRANSPORT) {
                        if (!f.getTransportAdverts().contains((TransportAdvert) advert)) {
                            f.getTransportAdverts().add((TransportAdvert) advert);
                            fileRepository.save(f);
                        }
                    }
                    if (advertType == AdvertType.ELECTRONICS) {
                        if (!f.getElectronicsAdverts().contains((ElectronicsAdvert) advert)) {
                            f.getElectronicsAdverts().add((ElectronicsAdvert) advert);
                            fileRepository.save(f);
                        }
                    }
                } else {
                    String path = environment.getRequiredProperty("file.upload.path");
                    String name = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                    java.io.File dir = new java.io.File(path);

                    while (checkExists(dir, name)) {
                        name = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                    }
                    java.io.File newFile = new java.io.File(path + name);
                    try (FileOutputStream outputStream = new FileOutputStream(newFile)) {
                        outputStream.write(file.getBytes());
                    }

                    if (advertType == AdvertType.TRANSPORT) {
                        fileRepository.save(File.builder()
                                .hash(hash)
                                .name(name)
                                .transportAdverts(Collections.singletonList((TransportAdvert) advert))
                                .size(file.getSize())
                                .build());
                    }
                    if (advertType == AdvertType.ELECTRONICS) {
                        fileRepository.save(File.builder()
                                .hash(hash)
                                .name(name)
                                .electronicsAdverts(Collections.singletonList((ElectronicsAdvert) advert))
                                .size(file.getSize())
                                .build());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void uploadUserFile(MultipartFile file, User user) {
        if (file == null || file.getSize() == 0) {
            return;
        }
        try {
            String hash = DigestUtils.md5Hex(file.getBytes());

            Optional<File> optionalFile = fileRepository.findByHashAndSize(hash, file.getSize());
            if (optionalFile.isPresent()) {
                user.setFile(optionalFile.get());
            } else {
                String path = environment.getRequiredProperty("file.upload.path");
                String name = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                java.io.File dir = new java.io.File(path);

                while (checkExists(dir, name)) {
                    name = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                }
                java.io.File newFile = new java.io.File(path + name);
                try (FileOutputStream outputStream = new FileOutputStream(newFile)) {
                    outputStream.write(file.getBytes());
                }
                
                File f = fileRepository.save(File.builder()
                        .hash(hash)
                        .name(name)
                        .size(file.getSize())
                        .build());

                user.setFile(f);
            }
            userService.update(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkExists(java.io.File dir, String name) {
        for (java.io.File f : dir.listFiles()) {
            if (f.isFile()) {
                if (f.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }
}
