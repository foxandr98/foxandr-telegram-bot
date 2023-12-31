package com.foxandr.telegrambot.commands.base;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.commands.CommandContainer;
import com.foxandr.telegrambot.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

@Slf4j
@Component
public class HelpCommand implements Command {
    @Value("${app.version}")
    private String version;
    private static String FULL_HELP_STRING;
    private CommandContainer commandContainer;

    public void initHelpCommand(CommandContainer commandContainer) {
        this.commandContainer = commandContainer;
    }

    @Override
    public String getName() {
        return "help";
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
        return new String[]{"help", "help [команда]"};
    }

    @Override
    public String[] getExamples() {
        return new String[]{"help", "help roll"};
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
        return 1;
    }

    public void initHelpString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>Лисобот v").append(version).append("</b>\n");
        sb.append("<b>Доступные команды!</b>\n");
        for (Command command : commandContainer.getSortedListOfCommands()) {
            createCommandBaseDescription(sb, command);
//            helpStringBuilder.append("Пример использования: " + command.getUsage() + "\n");
        }
        FULL_HELP_STRING = sb.toString();
        log.info("Help string created->\n {}", FULL_HELP_STRING);
    }

    public String createHelpStringWithArg(String commandName) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>Лисобот v").append(version).append("</b>\n");
        Command command;
        try {
            command = commandContainer.getCommandsByName().get(commandName);
            if (command == null)
                throw new NullPointerException();
        } catch (NullPointerException e) {
            log.info("Несуществующая команда /{}", commandName);
            throw new IllegalArgumentException();
        }
        createCommandBaseDescription(sb, command);
        sb.append("--------------------------\n");
        sb.append("Применение: \n");
        Arrays.stream(command.getUsage()).forEach(u -> sb.append("> <code>/").append(u).append("</code>\n"));
        sb.append("--------------------------\n");
        sb.append("Минимум аргументов: ").append(command.getMinArgs()).append("\n");
        sb.append("Максимум аргументов: ").append(command.getMaxArgs()).append("\n");
        sb.append("--------------------------\n");
        sb.append("Примеры использования: ");
        Arrays.stream(command.getExamples()).forEach(u -> sb.append("\n> <code>/").append(u).append("</code>"));
        if (command.getAliases().length != 0) {
            sb.append("\n--------------------------\n");
            sb.append("Псевдонимы: ");
            Arrays.stream(command.getAliases()).forEach(u -> sb.append("\n> <code>/").append(u).append("</code>"));
        }
        return sb.toString();
    }


    public void createCommandBaseDescription(StringBuilder sb, Command command) {
        sb.append("--------------------------\n");
        sb.append("Команда: <b>/" + command.getName() + "</b>\n");
        sb.append("Описание: " + command.getDescription() + "\n");
    }
}
