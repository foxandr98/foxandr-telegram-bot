package com.foxandr.telegrambot.commands.base;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class HelloCommand implements Command {
    @Override
    public String getName() {
        return "/hello";
    }

    @Override
    public String getDescription() {
        return "Сказать привет";
    }
    @Override
    public String[] getUsage() {
        return new String[] {"/hello"};
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/hi", "/ку"};
    }

    @Override
    public int getHelpOrderPriority() {
        return 2;
    }

    @Override
    public void execute(TelegramBot telegramBot, Message message, String[] args) throws TelegramApiException {
        telegramBot.execute(MessageUtils.createSendMessageWithText(message,
                "Привет, " + message.getFrom().getFirstName()));
    }
}
