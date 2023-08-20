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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class ReasonToDrinkCommand implements Command {
    private List<String> cachedCelebrations;
    private LocalDate cachedDate;

    @Override
    public String getName() {
        return "/reason_to_drink";
    }

    @Override
    public String getDescription() {
        return "Повод выпить";
    }

    @Override
    public String[] getUsage() {
        return new String[]{"/reason_to_drink"};
    }

    @Override
    public void execute(TelegramBot telegramBot, Message message, String[] args) throws TelegramApiException {
        fillCelebrationsAndCheckCacheIsActual(message);
        StringBuilder sendText = new StringBuilder("<b>Повод выпить!</b>");
        sendText.append(Emojis.BEER.getValue()).append(Emojis.CLINKING_GLASS.getValue()).append("\n");
        sendText.append("<b>Праздники сегодня: </b>\n");
        for (int i = 0; i < cachedCelebrations.size() && i < 5; i++)
            sendText.append("<i>").append(i + 1).append(") ").append(cachedCelebrations.get(i)).append("</i>\n");
        telegramBot.execute(MessageUtils.createSendMessageWithText(message, sendText.toString()));
    }

    private void fillCelebrationsAndCheckCacheIsActual(Message message) {
        LocalDate today = LocalDate.ofInstant(Instant.ofEpochSecond(message.getDate()), ZoneOffset.UTC);
        if (cachedCelebrations == null || today.isAfter(cachedDate)) {
            cachedDate = today;
            cachedCelebrations = getTodayCelebrations();
        }
    }

    public List<String> getTodayCelebrations() {
        String url = "https://kakoysegodnyaprazdnik.ru/";
        List<String> celebrates = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.getElementsByClass("main");
            for (Element element : elements) {
                String celebrating = element
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
