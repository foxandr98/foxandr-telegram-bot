package com.foxandr.telegrambot.commands.fun;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.enums.Emojis;
import com.foxandr.telegrambot.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class ReasonToDrinkCommand implements Command {
    @Override
    public String getName() {
        return "/reason_to_drink";
    }

    @Override
    public String getDescription() {
        return "Повод выпить";
    }

    @Override
    public void execute(TelegramBot telegramBot, Message message, String[] args) throws TelegramApiException {
        List<String> celebrations = getTodayCelebrations(5);
        StringBuilder sendText = new StringBuilder("<b>Повод выпить!</b>");
        sendText.append(Emojis.BEER.getValue()).append(Emojis.CLINKING_GLASS.getValue()).append("\n");
        sendText.append("<b>Праздники сегодня: </b>\n");
        for (int i = 0; i < celebrations.size(); i++)
            sendText.append("<i>").append(i + 1).append(") ").append(celebrations.get(i)).append("</i>\n");
        telegramBot.execute(MessageUtils.createSendMessageWithText(message, sendText.toString()));
    }

    public List<String> getTodayCelebrations(int n) {
        String url = "https://kakoysegodnyaprazdnik.ru/";
        List<String> celebrates = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.getElementsByClass("main");
            for (int i = 0; i < elements.size() && i < n; i++) {
                String celebrating = elements.get(i)
                        .getElementsByAttribute("itemprop")
                        .first()
                        .text();
                celebrates.add(celebrating);
            }
        } catch (IOException e) {
            log.error("Url {} not found", url, e);
        } catch (NullPointerException e) {
            log.error("Celebration text not found", e);
        }
        return celebrates;
    }
}
