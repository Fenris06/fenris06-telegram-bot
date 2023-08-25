package ru.fenris06.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.fenris06.service.AnswerConsumer;
@Service
@Slf4j
public class AnswerConsumerImpl implements AnswerConsumer {
    @Override
    public void consumer(SendMessage sendMessage) {

    }
}
