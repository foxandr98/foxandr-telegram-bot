package com.foxandr.telegrambot.commands.media;

import com.foxandr.telegrambot.bot.TelegramBot;
import com.foxandr.telegrambot.commands.Command;
import com.foxandr.telegrambot.exceptions.ImageNotFoundException;
import com.foxandr.telegrambot.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

@Slf4j
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
        return new String[]{"/image [до 5 слов]", "/image cute fat сat"};
    }

    @Override
    public int getMaxArgs() {
        return 5;
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void execute(TelegramBot telegramBot, Message message, String[] args) throws TelegramApiException {
        try {
            telegramBot.execute(MessageUtils.createPhotoMessage(message,
                    getImageByWord(telegramBot.getWebDriver(), args, 100)));
        } catch (ImageNotFoundException e) {
            log.info("Image by query [{}] not found", args[0]);
            telegramBot.execute(MessageUtils.createSendMessageWithText(message,
                    "Изображение по запросу: " + args[0] + " не найдено!"));
        }
    }

    public File getImageByWord(WebDriver webDriver, String[] args, int maxRandom) throws ImageNotFoundException {
        String query = String.join(" ", args);
        String url = String.format("https://ru.pinterest.com/search/pins/?q=%s", query);
        try {
            webDriver.get(url);

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("hCL")));

            Document doc = Jsoup.parse(webDriver.getPageSource());
            Elements elements = doc.getElementsByClass("hCL kVc L4E MIw");
            if (elements.size() == 0)
                throw new ImageNotFoundException(query);
            Random rnd = new Random();
            String dataSourceString = "";
            while (maxRandom > 0) {
                int randInt = rnd.nextInt(0, maxRandom + 1);
                if (elements.size() <= randInt) {
                    maxRandom /= 2;
                    continue;
                }
                dataSourceString = elements.get(randInt).attr("src");
                break;
            }
            String randomFileName = UUID.randomUUID().toString();
            File imageFile = new File("images/" + randomFileName + ".jpg");
            FileUtils.copyURLToFile(new URL(dataSourceString), imageFile);
            return imageFile;
        } catch (IOException e) {
            log.error("Download image error", e);
            throw new RuntimeException("Download image error", e);
        }
    }
}