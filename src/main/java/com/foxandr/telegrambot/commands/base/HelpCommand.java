package com.foxandr.telegrambot.commands.base;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.commands.CommandContainer;
import com.foxandr.telegrambot.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

@Slf4j
public class HelpCommand implements Command {
    public static String FULL_HELP_STRING;

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
    public String[] getUsage() {
        return new String[] {"/help", "/help [команда]"};
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public void execute(TelegramBot telegramBot, Message message, String[] args) throws TelegramApiException {
        String helpText;
        if (args.length == 0) {
            if (FULL_HELP_STRING == null)
                initHelpString();
            helpText = FULL_HELP_STRING;
        } else {
            helpText = createHelpStringWithArg(args[0]);
        }

        telegramBot.execute(MessageUtils.createSendMessageWithText(message, helpText));
    }

    @Override
    public int getHelpOrderPriority() {
        return 3;
    }

    //TODO Продумать, нужно ли реализовывать команды с аргументами внутри одного сообщения, или же отдельным сообщением
    public void initHelpString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>Лисобот v0.1</b>\n");
        sb.append("<b>Доступные команды!</b>\n");
        for (Command command : CommandContainer.getSetOfCommands()) {
            createCommandBaseDescription(sb, command);
//            helpStringBuilder.append("Пример использования: " + command.getUsage() + "\n");
        }
        FULL_HELP_STRING = sb.toString();
        log.info("Help string created->\n {}", FULL_HELP_STRING);
    }

    public String createHelpStringWithArg(String commandName) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>Лисобот v0.1</b>\n");
        Command command;
        try {
            command = CommandContainer.getCommandsByName().get(commandName);
        } catch (NullPointerException e) {
            log.info("Несуществующая команда {}", commandName);
            throw new IllegalArgumentException();
        }
        createCommandBaseDescription(sb, command);
        sb.append("--------------------------\n");
        sb.append("Примеры использования: \n");
        Arrays.stream(command.getUsage()).forEach(u -> sb.append("> <code>").append(u).append("</code>\n"));
        sb.append("Псевдонимы: \n");
        Arrays.stream(command.getAliases()).forEach(u -> sb.append("> <code>").append(u).append("</code>\n"));
        sb.append("Минимум аргументов: ").append(command.getMinArgs()).append("\n");
        sb.append("Максимум аргументов: ").append(command.getMaxArgs());
        return sb.toString();
    }


    public void createCommandBaseDescription(StringBuilder sb, Command command) {
        sb.append("--------------------------\n");
        sb.append("Команда: <b>" + command.getName() + "</b>\n");
        sb.append("Описание: " + command.getDescription() + "\n");
    }
}
