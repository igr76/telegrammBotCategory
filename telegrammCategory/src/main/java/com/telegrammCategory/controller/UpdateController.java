package com.telegrammCategory.controller;


import com.telegrammCategory.exception.ElemNotFound;
import com.telegrammCategory.model.Category;
import com.telegrammCategory.model.UserState;
import com.telegrammCategory.repository.CategoryRepository;
import com.telegrammCategory.service.CategoryService;
import com.telegrammCategory.service.UpdateProducer;
import com.telegrammCategory.service.UserService;
import com.telegrammCategory.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.telegrammCategory.controller.AllText.*;

/**  Контроллер Категорий  */
@Slf4j
@Component
public class UpdateController {
    private  TelegramBot telegramBot;
    private MessageUtils messageUtils;
    private UserState userState;
    private CategoryService categoryService;
    private UserService userService;
    private CategoryRepository categoryRepository;
    private UpdateProducer updateProducer;

    public UpdateController(@Lazy TelegramBot telegramBot, MessageUtils messageUtils,
                            UserState userState, CategoryService categoryService, UserService userService) {
        this.telegramBot = telegramBot;
        this.messageUtils = messageUtils;
        this.userState = userState;
        this.categoryService = categoryService;
        this.userService = userService;
    }


//    private void setUnssupportedMessageTipeView(Update update) {
//        var sendMessage = messageUtils.generateSendMessageWithText(update,
//                "Неподдерживаемый тип сообщения");
//                setView(sendMessage);
//    }



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
        log.info("distributeMessagesByType");
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
        updateProducer.produce(PHOTO_MESSAGE_UPDATE,update);
        setFileIsReceivedView(update);
    }
    private void processDocMessage(Update update) {
        updateProducer.produce(DDC_MESSAGE_UPDATE,update);
        setFileIsReceivedView(update);
    }
    private void processTextMessage(Update update) {
        String message = update.getMessage().getText();
        UserState userState = new UserState();
        userState =  userService.getUserState(update.getMessage().getChatId());
        String last_sction =userState.getLastAction();
        if (last_sction == null) {
            return;
        }
        Integer level = userState.getLevel();
        switch (last_sction) {
            case GREAT -> greatCategory(level,message,update);
            case GREAT_NEW -> greatNewCategory(level,message,update);
            case DELETE -> deleteCategory(level,message);
            case NEXT -> nextCategory(level,message,update);

        }
    }

    private void nextCategory(Integer level, String message,Update update) {
        try {
            int value = Integer.parseInt(message);
            Category category = categoryRepository.findByParentAndSeg(level,value).orElseThrow(ElemNotFound::new);
            String text = categoryService.getCategoryLevel(category.getId());
            telegramBot.sendAnswerTextMessage(update.getMessage().getChatId(),
                    text);

        } catch (NumberFormatException e) {
            telegramBot.sendAnswerTextMessage(update.getMessage().getChatId(),
                    "Неверные данные");
        }
    }

    private void deleteCategory(Integer level, String message) {
        log.info("deleteCategory");
        try {
            int Value = Integer.parseInt(message);
            categoryService.deleteCategory(Value,level);
        } catch (NumberFormatException e) {

        }
    }
    private void greatNewCategory(Integer level, String message,Update update) {
        try {
            int Value = Integer.parseInt(message);
            level = categoryService.newLevel(level,Value);
            userService.setLevelUserState(update.getMessage().getChatId(),level);
            telegramBot.sendAnswerTextMessage(update.getMessage().getChatId(),
                    "Введите имя новой категории");

        } catch (NumberFormatException e) {
            categoryService.greatCategory(level,message);
        }
//        UserState userState1 = new UserState();
//        userState1.setLevel(level);
//        userStateRepository.save(userState1);
    }

    private void greatCategory(Integer level, String message,Update update) {
        categoryService.greatCategory(level,message);
        telegramBot.sendAnswerTextMessage(update.getMessage().getChatId(),
                categoryService.getCategoryPreviousLevel(level));

    }


    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Файл получен! Обрабатывается...");
        setView(sendMessage);
    }
}
