package com.foxandr.telegrambot.commands.base;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StopCommand implements Command {
    @Override
    public String getName() {
        return "/stop";
    }

    @Override
    public String getDescription() {
        return "Отключить все подписки и таймеры";
    }

    @Override
    public String[] getUsage() {
        return new String[]{"/stop"};
    }

    @Override
    public void execute(TelegramBot telegramBot, Message message, String[] args) throws TelegramApiException {
        telegramBot.execute(MessageUtils.createSendMessageWithText(message, "Все подписки и таймеры отключены!"));
    }

    @Override
    public int getHelpOrderPriority() {
        return 2;
    }
}
