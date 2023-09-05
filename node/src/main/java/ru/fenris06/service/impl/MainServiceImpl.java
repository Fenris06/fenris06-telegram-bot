package ru.fenris06.service.impl;

import ru.fenris06.dao.AppUserRepository;
import ru.fenris06.entity.AppDocument;
import ru.fenris06.entity.AppPhoto;
import ru.fenris06.entity.AppUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.fenris06.dao.RawDataRepository;
import ru.fenris06.entity.RawData;
import ru.fenris06.entity.enums.UserState;
import ru.fenris06.exception.UploadFileException;
import ru.fenris06.service.AppUserService;
import ru.fenris06.service.FileService;
import ru.fenris06.service.MainService;
import ru.fenris06.service.ProduceService;
import ru.fenris06.service.enums.LinkType;
import ru.fenris06.service.enums.ServiceCommand;

import java.util.Optional;

import static ru.fenris06.entity.enums.UserState.BASIC_STATE;
import static ru.fenris06.entity.enums.UserState.WAIT_FORE_EMAIL_STATE;
import static ru.fenris06.service.enums.ServiceCommand.*;

@Service
@Log4j
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final RawDataRepository repository;
    private final ProduceService produceService;
    private final AppUserRepository appUserRepository;
    private final FileService fileService;
    private final AppUserService appUserService;

    @Override
    public void processTextMessage(Update update) {
        saveRAwData(update);
        AppUser appUser = findOurSaveAppUser(update);
        UserState userState = appUser.getState();
        String text = update.getMessage().getText();

        String output = "";

        ServiceCommand serviceCommand = ServiceCommand.fromValue(text);
        if (CANCEL.equals(serviceCommand)) {
            output = canceledProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FORE_EMAIL_STATE.equals(userState)) {
            output = appUserService.setEmail(appUser, text);
        } else {
            log.error("Unknown state error " + userState);
            output = "Unknown state error! Enter /cancel and try again";
        }
        Long chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);


    }

    @Override
    public void processDocMessage(Update update) {
        saveRAwData(update);
        AppUser appUser = findOurSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }
        try {
            AppDocument doc = fileService.processDoc(update.getMessage());
            String link = fileService.generateLink(doc.getId(), LinkType.GET_DOC);
            String answer = "Document is upload. Download link: " + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException e) {
            log.error(e);
            String error = "Sorry! File is not upload. Try again";
            sendAnswer(error, chatId);
        }

    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRAwData(update);
        AppUser appUser = findOurSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }

        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            String link = fileService.generateLink(photo.getId(), LinkType.GET_PHOTO);
            String answer = "Photo is upload. Download link: " + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException e) {
            log.error(e);
            String error = "Sorry! Photo is not upload. Try again";
            sendAnswer(error, chatId);
        }
    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        UserState userState = appUser.getState();
        if (!appUser.getIsActive()) {
            String error = "Having registration or activation your profile for uploading content";
            sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            String error = "Cancel the current command with /cancel for send file";
            sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        produceService.produceAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        ServiceCommand serviceCommand = ServiceCommand.fromValue(cmd);
        if (REGISTRATION.equals(serviceCommand)) {
            return appUserService.registerUser(appUser);
        } else if (HELP.equals(serviceCommand)) {
            return help();
        } else if (START.equals(serviceCommand)) {
            return "Hello! Enter /help to see available command";
        } else {
            return "Unknown command! Enter /help to see available command";
        }

    }

    private String help() {
        return "List of available command: \n"
                + "/cancel - cancel current command \n"
                + "/registration - user registration";
    }

    private String canceledProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserRepository.save(appUser);
        return "The command is canceled";
    }

    private AppUser findOurSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
        Optional<AppUser> optional = appUserRepository.findByTelegramUserId(telegramUser.getId());
        if (optional.isEmpty()) {
            AppUser transientUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .isActive(false)
                    .state(BASIC_STATE)
                    .build();
            return appUserRepository.save(transientUser);
        }
        return optional.get();
    }

    private void saveRAwData(Update update) {
        RawData rawData = new RawData();
        rawData.setEvent(update);
        repository.save(rawData);
    }
}
