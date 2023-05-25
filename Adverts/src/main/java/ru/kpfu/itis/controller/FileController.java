package ru.kpfu.itis.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.annotation.Log;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/file")
public class FileController {

    @Log
    @GetMapping("/{name}")
    public ResponseEntity<Resource> getFile(@Value("${file.upload.path}") String rootPath, @PathVariable String name) throws MalformedURLException {
        Resource resource = new FileUrlResource(rootPath + name);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }
}
