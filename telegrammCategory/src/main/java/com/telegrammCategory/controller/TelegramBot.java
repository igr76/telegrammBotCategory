package com.telegrammCategory.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    @Override
    public String getBotUsername() {
        return "igr7666666ibot";
    }

    @Override
    public String getBotToken() {
        return "6410289486:AAF4BigR_epjMbqHOt94o0U4SInPnbSCUG4";
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("cjjooih");
        var o = update.getMessage();
        System.out.println(o.getText());
    }
}
