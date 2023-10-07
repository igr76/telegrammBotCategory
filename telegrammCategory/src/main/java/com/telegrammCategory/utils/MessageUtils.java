package com.telegrammCategory.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageUtils {

    public SendMessage generateSendMessageWithText(Update update, String text) {
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        return sendMessage;
    }
//    public SendMessage generateSendMessageWithTextOfChatId(long ChatId, String text) {
//        var sendMessage = new SendMessage();
//        sendMessage.setChatId(Long.toString(ChatId));
//        sendMessage.setText(text);
//        return sendMessage;
//    }
}
