package ru.fenris06.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import ru.fenris06.dao.AppDocumentRepository;
import ru.fenris06.dao.AppPhotoRepository;
import ru.fenris06.entity.AppDocument;
import ru.fenris06.entity.AppPhoto;
import ru.fenris06.entity.BinaryContent;
import ru.fenris06.service.FileService;
import ru.fenris06.utils.CryptoTool;

import java.io.File;
import java.io.IOException;

@Service
@Log4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final AppDocumentRepository appDocumentRepository;
    private final AppPhotoRepository appPhotoRepository;
    private final CryptoTool cryptoTool;

    @Override
    public AppDocument getDocument(String hash) {
        Long id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appDocumentRepository.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String hash) {
        Long id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appPhotoRepository.findById(id).orElse(null);
    }

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {
            //TODO добавить генерацию имени временного файла
            File temp = File.createTempFile("tempFile", ".bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp, binaryContent.getFileAsArrayOfBytes());
            return new FileSystemResource(temp);
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
}
