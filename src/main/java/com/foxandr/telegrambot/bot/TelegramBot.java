package com.foxandr.telegrambot.bot;

import com.foxandr.telegrambot.commands.CommandContainer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    //    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);
    private final UpdateController updateController;
    @Value("${bot.name}")
    String botName;

    @Autowired
    public TelegramBot(@Value("${bot.token}") String botToken, UpdateController updateController) {
        super(botToken);
        this.updateController = updateController;

    }

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}