package com.foxandr.telegrambot.commands.base;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.commands.CommandContainer;
import com.foxandr.telegrambot.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class HelpCommand implements Command {
    public static String HELP_STRING;

    @Override
    public String getName() {
        return "/help";
    }

    @Override
    public String getDescription() {
        return "Показать справку по командам";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void execute(TelegramBot telegramBot, Message message, String[] args) throws TelegramApiException {
        if (HELP_STRING == null)
            initHelpString();
        telegramBot.execute(MessageUtils.createSendMessageWithText(message, HELP_STRING));
    }

    @Override
    public int getHelpOrderPriority() {
        return 3;
    }

    //TODO Продумать, нужно ли реализовывать команды с аргументами внутри одного сообщения, или же отдельным сообщением
    public void initHelpString() {
        StringBuilder helpStringBuilder = new StringBuilder();
        helpStringBuilder.append("<b>Лисобот v0.1</b>\n");
        helpStringBuilder.append("<b>Доступные команды!</b>\n");
        for (Command command : CommandContainer.getSetOfCommands()) {
            helpStringBuilder.append("--------------------------\n");
            helpStringBuilder.append("Команда: <b>" + command.getName() + "</b>\n");
            helpStringBuilder.append("Описание: " + command.getDescription() + "\n");
//            helpStringBuilder.append("Пример использования: " + command.getUsage() + "\n");
        }
        HELP_STRING = helpStringBuilder.toString();
        log.info("Help string created->\n {}", HELP_STRING);
    }
}
