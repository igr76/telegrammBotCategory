package com.telegrammCategory.controller;

import com.telegrammCategory.model.UserState;
import com.telegrammCategory.repository.UserStateRepository;
import com.telegrammCategory.service.CategoryService;
import com.telegrammCategory.service.UpdateProducer;
import com.telegrammCategory.service.UserService;
import com.telegrammCategory.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.telegrammCategory.controller.AllText.*;
/**  Контроллер Категорий  */
@Slf4j
@Component
public class UpdateController {
    private  TelegramBot telegramBot;
    private  MessageUtils messageUtils;
    private  UpdateProducer updateProducer;
    private CategoryService categoryService;
    private UserStateRepository userStateRepository;
    private UserService userService;

    public UpdateController(TelegramBot telegramBot, MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.telegramBot = telegramBot;
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    private void setUnssupportedMessageTipeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Неподдерживаемый тип сообщения");
                setView(sendMessage);
    }



    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Сообщение пустое");
            messageUtils.generateSendMessageWithText(update,
                    "Сообщение пустое");

            return;
        }
        if (update.getMessage() != null) {
            distributeMessagesByType(update);
        } else {log.error("Получено сообщение неподдерживаемого типа" + update);}
    }

    private void distributeMessagesByType(Update update) {
        var message = update.getMessage();
        if (message.hasText()) {
            processTextMessage(update);
        } else if (message.hasDocument()) {
            processDocMessage(update);
        } else if (message.hasPhoto()) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }
    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Неподдерживаемый тип сообщения!");
        setView(sendMessage);
    }
    private void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(AllText.PHOTO_MESSAGE_UPDATE,update);
        setFileIsReceivedView(update);
    }
    private void processDocMessage(Update update) {
        updateProducer.produce(AllText.DDC_MESSAGE_UPDATE,update);
        setFileIsReceivedView(update);
    }
    private void processTextMessage(Update update) {
        String message = update.getMessage().getText();
        UserState userState = new UserState();
        userState =  userService.getUserState(update.getUpdateId());
        String last_sction =userState.getLastAction();
        Integer level = userState.getLevel();
        switch (last_sction) {
            case GREAT -> greatCategory(level,message);
            case GREAT_NEW -> greatNewCategory(level,message);
            case DELETE -> deleteCategory(level,message);

            }
        }

    private void deleteCategory(Integer level, String message) {
        try {
            int Value = Integer.parseInt(message);
             categoryService.deleteCategory(Value,level);
        } catch (NumberFormatException e) {

        }
    }
    private void greatNewCategory(Integer level, String message) {
        try {
            int Value = Integer.parseInt(message);
            level = categoryService.newLevel(level,Value);
        } catch (NumberFormatException e) {
            categoryService.greatCategory(level,message);
        }
        UserState userState1 = new UserState();
        userState1.setLevel(level);
        userStateRepository.save(userState1);
    }

    private void greatCategory(Integer level, String message) {
        categoryService.greatCategory(level,message);
    }


    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Файл получен! Обрабатывается...");
        setView(sendMessage);
    }
}
