package ru.fenris06.controller;


import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String name;
    @Value("${bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message text = update.getMessage();
        log.debug(text.getText());

        SendMessage response = new SendMessage();
        response.setChatId(text.getChatId().toString());
        response.setText("Hello fom bot");
        sendAnswerMessage(response);
    }

    public void sendAnswerMessage(SendMessage message) {

            if (message != null) {
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    log.error(e);
                }

            }

    }
}
