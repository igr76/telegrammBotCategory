package com.telegrammCategory.controller;

import com.telegrammCategory.model.UserState;
import com.telegrammCategory.repository.UserStateRepository;
import com.telegrammCategory.service.CategoryService;
import com.telegrammCategory.service.UserService;
import com.telegrammCategory.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.telegrammCategory.controller.AllText.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final UpdateController updateController;
    private final CategoryService categoryService;
    private final MessageUtils messageUtils;
    private  final UserService userService;
    private  final UserStateRepository userStateRepository;

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private  String botToken;

    public    String LAST_ACTION ;
    public    Integer level ;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }



    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(String.valueOf(e));
            }
        }
    }
    public void sendAnswerTextMessage(Long chatId,String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendAnswerMessage(sendMessage);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();

        if (userService.getUserState(chatId) == null) {
            level = 1; UserState userState = new UserState(); userState.setId(chatId);userState.setLevel(level);
            userService.updateUserState(userState);
        }else    level = userService.getLevelUserState(chatId);

        switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
            case GREAT -> greatCommand(chatId);
            case GREAT_NEW -> greatNewCommand(chatId);
            case GET -> getCommand(chatId);
            case DELETE -> deleteCommand(chatId);
            case HELP -> helpCommand(chatId);
            case NEXT -> nextCommand(chatId);
            case PREVIOUS -> previousCommand(chatId);
            default -> unknownCommand(chatId);
        }
        updateController.processUpdate(update);
    }

    private void greatNewCommand(Long chatId) {
        sendAnswerTextMessage(chatId,"Введите номер расширяемой категории");
        userService.saveUserLastAction(GREAT_NEW,chatId);
    }

    private void previousCommand(Long chatId) {
       level= categoryService.getCategoryPreviousLevel(level);
       UserState userState = new UserState();
       userState.setLevel(level);
    }

    private void nextCommand(Long chatId) {
        sendAnswerTextMessage(chatId,"В какое меню перейти?");
        userService.saveUserLastAction(NEXT,chatId);
    }

    private void getCommand(Long chatId) {
        sendAnswerTextMessage(chatId,categoryService.getCategoryLevel(level));
        LAST_ACTION = GET;
    }

    private void greatCommand(Long chatId) {
        sendAnswerTextMessage(chatId,"Введите название категории");
        userService.saveUserLastAction(GREAT,chatId);

    }

    private void deleteCommand(long chatId) {
        sendAnswerTextMessage(chatId,"Введите название категории");
        userService.saveUserLastAction(DELETE,chatId);
    }

    private void startCommand(Long chatId, String userName) {
        var text = """
                Добро пожаловать в бот, %s!
                
                Здесь Вы сможете создавать и удалять категории товаров.
                
                Для этого воспользуйтесь командами:
                /get - получить список категорий
                /great - создать категорию
                /delete - удалить категорию
                 /greatNew- Cоздать новое меню
                Дополнительные команды:
                /help - получение справки
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }
    private void helpCommand(Long chatId) {
        var text = """
                Справочная информация по боту
                
               Здесь Вы сможете создавать и удалять категории товаров.
                
                Для этого воспользуйтесь командами:
                /get - получить список категорий
                /great - создать категорию
                /greatNew- Cоздать новое меню
                /delete - удалить категорию
                /help - получение справки
                """;
        sendMessage(chatId, text);
    }
    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }
    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения", e);
        }
    }





}
