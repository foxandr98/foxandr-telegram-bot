package com.foxandr.telegrambot.commands.base;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StartCommand extends Command {
    public StartCommand(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public String getName() {
        return "/start";
    }

    @Override
    public String getDescription() {
        return "Скажи привет!";
    }

    @Override
    public String getUsage() {
        return "/start";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "/hi" };
    }

    @Override
    public void execute(Message message, String[] args) throws TelegramApiException {
        telegramBot.execute(MessageUtils.createSendMessageWithText(message,
                "Привет, " + message.getFrom().getFirstName()));

    }
}
