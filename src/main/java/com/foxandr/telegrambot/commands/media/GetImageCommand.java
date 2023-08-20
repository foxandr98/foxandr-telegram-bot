package com.foxandr.telegrambot.commands.media;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.util.MessageUtils;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

public class GetImageCommand implements Command {
    @Override
    public String getName() {
        return "/image";
    }

    @Override
    public String getDescription() {
        return "Получить изображение по слову";
    }

    @Override
    public String[] getUsage() {
        return new String[]{"/image [слово]", "/image cute_fat_cat"};
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public void execute(TelegramBot telegramBot, Message message, String[] args) throws TelegramApiException {
        telegramBot.execute(MessageUtils.createPhotoMessage(message, getImageByWord(args[0], 20)));
    }

    public File getImageByWord(String word, int maxRandom) {
        String url = String.format("https://yandex.ru/images/search?text=%s", word);
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.getElementsByClass("serp-item__thumb justifier__thumb");
            Random rnd = new Random();
            String dataSourceString = elements.get(rnd.nextInt(maxRandom)).attr("src");
            String randomFileName = UUID.randomUUID().toString();
            File imageFile = new File("images/" + randomFileName + ".webp");
            FileUtils.copyURLToFile(new URL("https:" + dataSourceString), imageFile);
            return imageFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
