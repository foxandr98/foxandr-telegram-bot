package com.foxandr.telegrambot.util;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
public class MessageUtils {
    private MessageUtils(){}
    public static SendMessage createSendMessageWithText(Message message, String text){
        log.info("Sending answer in [{}] to [@{}] -> {}",
                ChatUtils.getGroupChatOrUserName(message.getChat()), message.getFrom().getUserName(), text);
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId());
        sendMessage.setText(text);
        return sendMessage;
    }
}
