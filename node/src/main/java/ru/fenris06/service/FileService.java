package ru.fenris06.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fenris06.entity.AppDocument;

public interface FileService {
    AppDocument processDoc(Message externalMessage);
}
