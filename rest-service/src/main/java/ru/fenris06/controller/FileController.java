package ru.fenris06.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fenris06.entity.AppDocument;
import ru.fenris06.entity.AppPhoto;
import ru.fenris06.entity.BinaryContent;
import ru.fenris06.service.FileService;

@RestController
@RequestMapping("/file")
@Log4j
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/get-doc")
    public ResponseEntity<Object> getDocument(@RequestParam("id") String id) {
        //TODO сделать рефакторинг кода. Перенести в сервис.
        AppDocument document = fileService.getDocument(id);
        if (document == null) {
            return ResponseEntity.badRequest().build();
        }

        BinaryContent binaryContent = document.getBinaryContent();

        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getMimeType()))
                .header("Content-disposition", "attachment; filename=" + document.getDocName())
                .body(fileSystemResource);
    }

    @GetMapping("/get-photo")
    public ResponseEntity<Object> getPhoto(@RequestParam("id") String id) {
        //TODO сделать рефакторинг кода. Перенести в сервис.
        AppPhoto photo = fileService.getPhoto(id);
        if (photo == null) {
            return ResponseEntity.badRequest().build();
        }

        BinaryContent binaryContent = photo.getBinaryContent();

        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header("Content-disposition", "attachment;")
                .body(fileSystemResource);
    }
}
