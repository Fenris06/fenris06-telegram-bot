package ru.fenris06.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {
    void consumerTextMessageUpdates(Update update);

    void consumerDocMessageUpdates(Update update);

    void consumerPhotoMessageUpdates(Update update);
}
