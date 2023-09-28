package com.telegrammCategory;

import com.telegrammCategory.controller.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TelegrammCategoryApplication {

    public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(TelegrammCategoryApplication.class, args);

		List<BotCommand> listofCommands = new ArrayList<>();
		listofCommands.add(new BotCommand("/next", "Следующий"));
		listofCommands.add(new BotCommand("/previous", "Вернуться"));

	}


}
