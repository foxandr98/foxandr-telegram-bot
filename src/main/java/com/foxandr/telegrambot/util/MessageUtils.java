package com.foxandr.telegrambot.util;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;

@Slf4j
public class MessageUtils {
    private MessageUtils(){}
    public static SendMessage createSendMessageWithText(Message message, String text){
        log.info("Sending answer in [{}] to [@{}] -> {}",
                ChatUtils.getGroupChatOrUserName(message.getChat()), message.getFrom().getUserName(), text);
        var sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(text);
        return sendMessage;
    }

    public static SendPhoto createPhotoMessage(Message message, File file) {
        log.info("Sending photo in [{}] to [@{}] -> {}",
                ChatUtils.getGroupChatOrUserName(message.getChat()), message.getFrom().getUserName(), file.getName());
        var sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId());
        sendPhoto.setPhoto(new InputFile(file));
        return sendPhoto;
    }
}
