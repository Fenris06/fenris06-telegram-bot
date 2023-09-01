package ru.fenris06.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fenris06.entity.AppDocument;
import ru.fenris06.entity.AppPhoto;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
}
