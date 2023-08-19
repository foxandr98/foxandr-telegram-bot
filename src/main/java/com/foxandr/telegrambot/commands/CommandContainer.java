package com.foxandr.telegrambot.commands;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.base.StartCommand;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class CommandContainer {
    private final Map<String, Command> commandsByName;

    public CommandContainer(TelegramBot telegramBot) {
        commandsByName = new HashMap<>();
        Reflections reflections = new Reflections("com.foxandr.telegrambot.commands");
        Set<Class<? extends Command>> commandClasses = reflections.getSubTypesOf(Command.class);
        for (Class<? extends Command> commandClass : commandClasses) {
            log.info("COMMAND [{}] added to command container!", commandClass.getName());
            try {
                Command command = commandClass.getConstructor(TelegramBot.class).newInstance(telegramBot);
                addCommand(command);
            } catch (Exception e) {
                log.error("Error while adding {} command to command container", commandClass.getName(), e);
            }
        }
    }

    private void addCommand(Command command) {
        commandsByName.put(command.getName(), command);
    }

    public Command retrieveCommand(String commandName) {
        return commandsByName.get(commandName);
    }
}
