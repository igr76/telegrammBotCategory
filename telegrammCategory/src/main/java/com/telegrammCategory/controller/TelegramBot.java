package com.telegrammCategory.controller;

import com.telegrammCategory.model.UserState;
import com.telegrammCategory.service.CategoryService;
import com.telegrammCategory.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static com.telegrammCategory.controller.AllText.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private  UpdateController updateController;
    private UserService userService;
    private UserState userState;
    private CategoryService categoryService;

    public TelegramBot(@Lazy UpdateController updateController, UserService userService, CategoryService categoryService) {
        this.updateController = updateController;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    public    String LAST_ACTION ;
    public    Integer level =1;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (userService.getUserState(chatId) == null) {
                level = 1; UserState userState = new UserState(); userState.setId(chatId);userState.setLevel(level);
                userService.updateUserState(userState,chatId);
            }else    level = userService.getLevelUserState(chatId);
            switch (messageText) {
                case START -> {
                    String userName = update.getMessage().getChat().getUserName();
                    startCommand(chatId, userName);
                }
                case ADD -> greatCommand(messageText);
                case GET -> getCommand(chatId);
                case DELETE -> deleteCommand(messageText);
                case HELP -> helpCommand(chatId);
                case VIEW_TREE -> viewTreeCommand(chatId);
                default -> unknownCommand(update);
            }
        }
    }

    private void viewTreeCommand(long chatId) {
        sendMessage(chatId, categoryService.viewTree());
    }

    private void getCommand(long chatId) {
        sendMessage(chatId, categoryService.getCategoryLevel(level));

    }

    private void unknownCommand(Update update) {
        log.info("unknownCommand");
        updateController.processUpdate(update);

    }


    private void helpCommand(long chatId) {
        var text = """
                Справочная информация по боту
                
               Здесь Вы сможете создавать и удалять категории товаров.
                
                Для этого воспользуйтесь командами:
                /get - получить список категорий
                /great - создать категорию
                /greatNew - создать категорию в категории
                /delete - удалить категорию
                /next - перейти ниже
                /previous - перейти выше
                """;
        sendMessage(chatId, text);
    }

    private void deleteCommand(String text) {
        String textCommand = text.substring(12);
        categoryService.deleteCategory(textCommand);
    }

    private void greatNewCommand(String fatherCategory,String childrenCategory) {
        categoryService.addTwo(fatherCategory,childrenCategory);
    }

    private void greatCommand(String text) {
        String textCommand = text.substring(12);
        String[] textArray = textCommand.split(" ");
        if (textArray.length > 1) {greatNewCommand(textArray[0],textArray[1]);}
        categoryService.addOne(textCommand);
    }


    private void startCommand(long chatId, String userName) {
        var text = """
                Добро пожаловать в бот, %s!
                
                Здесь Вы сможете создавать и удалять категории товаров.
                
                Для этого воспользуйтесь минимальными командами:
                /get - получить список категорий
                /great - создать категорию
                /delete - удалить категорию
                
                Дополнительные команды:
                /help - получение справки обо всех командах
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    @Override
    public String getBotToken() {
        return  botToken;
    }
    @Override
    public String getBotUsername() {
        return botName;
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
    public void sendAnswerTextMessage(Long chatId,String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendAnswerMessage(sendMessage);
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
}