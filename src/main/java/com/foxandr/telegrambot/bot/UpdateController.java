package com.foxandr.telegrambot.bot;

import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.commands.CommandContainer;
import com.foxandr.telegrambot.exceptions.InvalidArgsCountException;
import com.foxandr.telegrambot.util.MessageUtils;
import com.foxandr.telegrambot.util.ChatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

@Component
@Slf4j
public class UpdateController {
    private TelegramBot telegramBot;
    private CommandContainer commandContainer;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
        this.commandContainer = new CommandContainer(telegramBot);
    }

    public void processUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                processTextMessage(message);
            } else if (message.hasVoice()) {
                processVoiceMessage(message);
            }
        }
    }

    private void processTextMessage(Message message) {
        String messageText = message.getText();
        if (ChatUtils.isGroupChat(message.getChat())
                && !messageText.matches("^(/\\w+@foxandr_bot\\b).*"))
            return;

        String[] args = messageText.split(" ");
        String command = args[0].split("@")[0].toLowerCase();
        String[] nArgs = Arrays.copyOfRange(args, 1, args.length);

        log.info("[{}] [{}] -> {}", ChatUtils.getGroupChatOrUserName(message.getChat()),
                String.format("@%s", message.getFrom().getUserName()), messageText);
        Command parsedCommand;
        try {
            try {
                parsedCommand = commandContainer.retrieveCommand(command);
                isArgsLegit(parsedCommand, nArgs);
                parsedCommand.execute(message, nArgs);
            } catch (NullPointerException e) {
                log.info("Unsupported command {} in [{}] from [@{}]",
                        command, ChatUtils.getGroupChatOrUserName(message.getChat()), message.getFrom().getUserName());
                telegramBot.execute(MessageUtils.createSendMessageWithText(message,
                        String.format("Команда %s не существует!", command)));
            } catch (InvalidArgsCountException e) {
                log.info("Invalid args count of {} command in [{}] from [@{}]. {}",
                        command, ChatUtils.getGroupChatOrUserName(message.getChat()), message.getFrom().getUserName(),
                        e.getMessage());
                telegramBot.execute(MessageUtils.createSendMessageWithText(message,
                        String.format("Неправильное число аргументов для команды %s", command)));
            }
        } catch (TelegramApiException e) {
            log.error("Failed to send message in [{}] to [@{}]",
                    ChatUtils.getGroupChatOrUserName(message.getChat()), message.getFrom().getUserName(), e);
        }
    }

    private void isArgsLegit(Command parsedCommand, String[] nArgs) throws InvalidArgsCountException {
        if (nArgs.length < parsedCommand.getMinArgs() || nArgs.length > parsedCommand.getMaxArgs())
            throw new InvalidArgsCountException(parsedCommand, nArgs.length);
    }

    private void processVoiceMessage(Message message) {
        //TODO Реализация обработки голосовых сообщений
    }
}
