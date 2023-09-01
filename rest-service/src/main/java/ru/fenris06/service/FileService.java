package ru.fenris06.service;

import org.springframework.core.io.FileSystemResource;
import ru.fenris06.entity.AppDocument;
import ru.fenris06.entity.AppPhoto;
import ru.fenris06.entity.BinaryContent;

public interface FileService {
    AppDocument getDocument(String id);

    AppPhoto getPhoto(String id);

    FileSystemResource getFileSystemResource(BinaryContent binaryContent);

}
