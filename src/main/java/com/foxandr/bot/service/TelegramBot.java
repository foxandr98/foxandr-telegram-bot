package com.foxandr.bot.service;

import com.foxandr.bot.util.TelegramChatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
//    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);
    @Value("${bot.name}")
    String botName;
    public TelegramBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            Message message = update.getMessage();
            Chat chat = message.getChat();
            User user = message.getFrom();
            String messageText = message.getText();
            log.info("[{}] [{}] -> {}",
                    TelegramChatUtils.getGroupChatOrUserName(chat),
                    String.format("@%s", user.getUserName()), messageText);
            switch(messageText){
                case "/start":
                    startCommandReceived(chat, user);
                    break;
                default:
                    sendAnswerMessage(chat, user,"Sorry, command is not supported");
                    break;
            }
        }
    }

    private void startCommandReceived(Chat chat, User user){
        String responseAnswer = String.format("Hi, %s!", user.getFirstName());
        sendAnswerMessage(chat, user, responseAnswer);
    }

    private void sendAnswerMessage(Chat chat, User user, String text){
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText(text);
        try{
            log.info("Sending answer in [{}] to [@{}] -> {}", TelegramChatUtils.getGroupChatOrUserName(chat), user.getUserName(), text);
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send message in [{}] to [@{}]", TelegramChatUtils.getGroupChatOrUserName(chat), user.getUserName(), e);
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getBotUsername() {
        return botName;
    }
}
