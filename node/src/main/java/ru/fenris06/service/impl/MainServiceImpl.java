package ru.fenris06.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fenris06.dao.RawDataRepository;
import ru.fenris06.entity.RawData;
import ru.fenris06.service.MainService;
import ru.fenris06.service.ProduceService;

@Service
@Log4j
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final RawDataRepository repository;
    private final ProduceService produceService;

    @Override
    public void processTextMessage(Update update) {
        saveRAwData(update);
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Hello from Node");
        produceService.produceAnswer(sendMessage);
    }

    private void saveRAwData(Update update) {
        RawData rawData = new RawData();
        rawData.setEvent(update);
        repository.save(rawData);
    }
}
