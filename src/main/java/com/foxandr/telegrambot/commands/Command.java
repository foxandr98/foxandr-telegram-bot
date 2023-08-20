package com.foxandr.telegrambot.commands;

import com.foxandr.telegrambot.bot.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface Command {
    String getName();
    String getDescription();
    String[] getUsage();
    default String[] getAliases() {
        return new String[0];
    }
    default int getMinArgs(){
        return 0;
    }
    default int getMaxArgs(){
        return 0;
    }

    default boolean isListedInHelp(){
        return true;
    }

    default int getHelpOrderPriority(){
        return 10;
    }

    void execute(TelegramBot telegramBot, Message message, String[] args) throws TelegramApiException;
}
