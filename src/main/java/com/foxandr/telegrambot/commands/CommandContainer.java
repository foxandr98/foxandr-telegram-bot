package com.foxandr.telegrambot.commands;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.base.StartCommand;
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
    public static String HELP_STRING;
    private final Map<String, Command> commandsByName;
    private final TelegramBot telegramBot;

    public CommandContainer(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
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
        initHelpString();
        initCommandMenu();
    }


    private void addCommand(Command command) {
        commandsByName.put(command.getName(), command);
        for (String alias : command.getAliases()) {
            commandsByName.put(alias, command);
        }
    }

    public Command retrieveCommand(String commandName) {
        return commandsByName.get(commandName);
    }

    public TreeSet<Command> getSetOfCommands() {
        TreeSet<Command> sortedCommandSet = new TreeSet<>(
                (c1, c2) -> Integer.compare(c1.getHelpOrderPriority(), c2.getHelpOrderPriority())
        );
        sortedCommandSet.addAll(commandsByName.values());
        return sortedCommandSet;
    }

    //TODO Продумать, нужно ли реализовывать команды с аргументами внутри одного сообщения, или же отдельным сообщением
    public void initHelpString() {
        StringBuilder helpStringBuilder = new StringBuilder();
        helpStringBuilder.append("<b>Лисобот v0.1</b>\n");
        helpStringBuilder.append("<b>Доступные команды!</b>\n");
        for (Command command : getSetOfCommands()) {
            helpStringBuilder.append("--------------------------\n");
            helpStringBuilder.append("Команда: <code>" + command.getName() + "</code>\n");
            helpStringBuilder.append("Описание: " + command.getDescription() + "\n");
//            helpStringBuilder.append("Пример использования: " + command.getUsage() + "\n");
        }
        HELP_STRING = helpStringBuilder.toString();
        log.info("Help string created->\n {}", HELP_STRING);
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
