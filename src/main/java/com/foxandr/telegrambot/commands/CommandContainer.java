package com.foxandr.telegrambot.commands;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.base.HelpCommand;
import com.foxandr.telegrambot.commands.base.StartCommand;
import com.foxandr.telegrambot.enums.Emojis;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
public class CommandContainer {
    private final TelegramBot telegramBot;
    private final static Map<String, Command> commandsByName;

    static {
        commandsByName = new HashMap<>();
        Reflections reflections = new Reflections("com.foxandr.telegrambot.commands");
        Set<Class<? extends Command>> commandClasses = reflections.getSubTypesOf(Command.class);
        for (Class<? extends Command> commandClass : commandClasses) {
            try {
                Command command = commandClass.getConstructor().newInstance();
                addCommand(command);
                log.info("COMMAND [{}] added to command container!", commandClass.getName());
            } catch (Exception e) {
                log.error("Error while adding {} command to command container", commandClass.getName(), e);
            }
        }
    }

    public static TreeSet<Command> getSetOfCommands() {
        TreeSet<Command> sortedCommandSet = new TreeSet<>(
                (c1, c2) -> Integer.compare(c1.getHelpOrderPriority(), c2.getHelpOrderPriority())
        );
        sortedCommandSet.addAll(commandsByName.values());
        return sortedCommandSet;
    }

    private static void addCommand(Command command) {
        commandsByName.put(command.getName(), command);
        for (String alias : command.getAliases()) {
            commandsByName.put(alias, command);
        }
    }

    public CommandContainer(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
        initCommandMenu();
    }

    public Command retrieveCommand(String commandName) {
        return commandsByName.get(commandName);
    }

    private void initCommandMenu() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        getSetOfCommands().forEach(com -> listOfCommands.add(
                        new BotCommand(
                                com.getName(),
                                com.getDescription())
                )
        );
        try {
            telegramBot.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's list command menu", e);
        }
        log.info("Menu of commands created");
    }
}
