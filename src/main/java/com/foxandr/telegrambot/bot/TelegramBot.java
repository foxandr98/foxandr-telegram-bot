package com.foxandr.telegrambot.bot;

import com.foxandr.telegrambot.commands.CommandContainer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    //    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);
    private final UpdateController updateController;
    private final CommandContainer commandContainer;

    @Value("${bot.name}")
    String botName;

    @Autowired
    public TelegramBot(@Value("${bot.token}") String botToken,
                       UpdateController updateController,
                       CommandContainer commandContainer) {
        super(botToken);
        this.updateController = updateController;
        this.commandContainer = commandContainer;
        initCommandMenu();
    }

    @PostConstruct
    public void fillUpdateController() {
        updateController.initUpdateController(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private void initCommandMenu() {
        List<BotCommand> listOfCommands = commandContainer.getSortedListOfCommands()
                .stream()
                .map(e -> new BotCommand(e.getName(), e.getDescription()))
                .collect(Collectors.toList());
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's list command menu", e);
        }
        log.info("Menu of commands created");
    }
}