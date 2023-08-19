package com.foxandr.telegrambot.commands.base;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.commands.CommandContainer;
import com.foxandr.telegrambot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class HelpCommand extends Command {
    public HelpCommand(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public String getName() {
        return "/help";
    }

    @Override
    public String getDescription() {
        return "Показать справку по командам";
    }

    @Override
    public String getUsage() {
        return "/help";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void execute(Message message, String[] args) throws TelegramApiException {
        telegramBot.execute(MessageUtils.createSendMessageWithText(message, CommandContainer.HELP_STRING));
    }
}
