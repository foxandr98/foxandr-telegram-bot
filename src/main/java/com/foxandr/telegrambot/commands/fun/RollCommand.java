package com.foxandr.telegrambot.commands.fun;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.enums.Emojis;
import com.foxandr.telegrambot.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Random;

@Slf4j
@Component
public class RollCommand implements Command {
    @Override
    public String getName() {
        return "roll";
    }

    @Override
    public String getDescription() {
        return "Кинуть кубик";
    }

    @Override
    public String[] getUsage() {
        return new String[]{"roll", "roll [max]", "roll [min] [max]"};
    }

    @Override
    public String[] getExamples() {
        return new String[]{"roll", "roll 6", "roll 1 20"};
    }

    @Override
    public String[] getAliases() {
        return new String[]{"dice"};
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public void execute(TelegramBot telegramBot, Message message, String[] args) throws TelegramApiException {
        int result;
        Random rnd = new Random();
        try {
            if (args.length == 0)
                result = roll(rnd);
            else if (args.length == 1)
                result = roll(rnd, Integer.parseInt(args[0]));
            else
                result = roll(rnd, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } catch (Exception e) {
            log.info("Некорректные аргументы {}", args);
            throw new IllegalArgumentException();
        }
        telegramBot.execute(MessageUtils.createSendMessageWithText(message,
                String.format("%s: %2$s %3$d %2$s", message.getFrom().getFirstName(), Emojis.DICE.getValue(), result)));
    }

    private int roll(Random rnd) {
        return roll(rnd, 1, 100);
    }

    private int roll(Random rnd, int max) {
        return roll(rnd, 1, max);
    }

    private int roll(Random rnd, int min, int max) {
        return rnd.nextInt(min, max + 1);
    }
}
