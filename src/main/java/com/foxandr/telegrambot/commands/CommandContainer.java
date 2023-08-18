package com.foxandr.telegrambot.commands;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.base.StartCommand;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CommandContainer {
    private final Map<String, Command> commandsByName;

    public CommandContainer(TelegramBot telegramBot) {
        commandsByName = new HashMap<>();
        addCommand(new StartCommand(telegramBot));
    }

    private void addCommand(Command command) {
        commandsByName.put(command.getName(), command);
    }

    public Command retrieveCommand(String commandName) {
        return commandsByName.get(commandName);
    }
}
