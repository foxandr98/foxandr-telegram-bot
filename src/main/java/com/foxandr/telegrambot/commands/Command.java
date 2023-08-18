package com.foxandr.telegrambot.commands;

import com.foxandr.telegrambot.bot.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class Command {
    protected TelegramBot telegramBot;
    public Command(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public abstract String getName();
    public abstract String getDescription();
    public abstract String getUsage();
    public abstract String[] getAliases();

    public int getMinArgs(){
        return 0;
    }

    public int getMaxArgs(){
        return 0;
    }

    public boolean isListedInHelp(){
        return true;
    }

    public abstract void execute(Message message, String[] args) throws TelegramApiException;
}
