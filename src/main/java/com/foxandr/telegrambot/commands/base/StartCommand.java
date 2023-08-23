package com.foxandr.telegrambot.commands.base;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.util.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class StartCommand implements Command {
    @Override
    public String getName() {
        return "start";
    }
    @Override
    public String getDescription() {
        return "Активировать бота";
    }
    @Override
    public String[] getUsage() {
        return new String[] {"start"};
    }

    @Override
    public void execute(TelegramBot telegramBot, Message message, String[] args) throws TelegramApiException {
        telegramBot.execute(MessageUtils.createSendMessageWithText(message, "Бот активирован!"));
    }
    @Override
    public int getHelpOrderPriority() {
        return 0;
    }
}
