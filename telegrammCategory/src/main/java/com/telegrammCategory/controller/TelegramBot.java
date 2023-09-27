package com.telegrammCategory.controller;

import com.telegrammCategory.model.UserState;
import com.telegrammCategory.repository.UserStateRepository;
import com.telegrammCategory.service.CategoryService;
import com.telegrammCategory.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.telegrammCategory.controller.AllText.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private UpdateController updateController;
    private CategoryService categoryService;
    private MessageUtils messageUtils;
    private UserStateRepository userStateRepository;

    public TelegramBot() {
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/next", "Следующий"));
        listofCommands.add(new BotCommand("/previous", "Вернуться"));
    }

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    public static   String LAST_ACTION ;
    public static   Integer level ;

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

        if (userStateRepository.findLevelById(chatId) == null) {
            level = 1; UserState userState = new UserState(); userState.setId(chatId);userState.setLevel(level);
            userStateRepository.save(userState);
        }else    level = userStateRepository.findLevelById(chatId);

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
        LAST_ACTION = GREAT_NEW;
    }

    private void previousCommand(Long chatId) {
       level= categoryService.getCategoryPreviousLevel(level);
       UserState userState = new UserState();
       userState.setLevel(level);
    }

    private void nextCommand(Long chatId) {
        sendAnswerTextMessage(chatId,"В какое меню перейти?");
        LAST_ACTION = NEXT;
    }

    private void getCommand(Long chatId) {
        sendAnswerTextMessage(chatId,categoryService.getCategoryLevel(level));
        LAST_ACTION = GET;
    }

    private void greatCommand(Long chatId) {
        sendAnswerTextMessage(chatId,"Введите название категории");
        LAST_ACTION = GREAT;

    }

    private void deleteCommand(long chatId) {
        sendAnswerTextMessage(chatId,"Введите название категории");
        LAST_ACTION = DELETE;
    }

    private void startCommand(Long chatId, String userName) {
        var text = """
                Добро пожаловать в бот, %s!
                
                Здесь Вы сможете создавать и удалять категории товаров.
                
                Для этого воспользуйтесь командами:
                /get - получить список категорий
                /great - создать категорию
                /delete - удалить категорию
                
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
                /delete - удалить категорию
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
