package ru.fenris06.service;

import ru.fenris06.dto.MailParams;

public interface MailSenderService {
    void send(MailParams mailParams);
}
