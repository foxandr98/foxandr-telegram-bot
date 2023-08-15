package com.foxandr.bot.util;

import org.telegram.telegrambots.meta.api.objects.Chat;

public final class TelegramChatUtils {
    private TelegramChatUtils(){}

    public static String getGroupChatOrUserName(Chat chat){
        return chat.isGroupChat() ? "Group chat: "+chat.getTitle() : "Private message";
    }
}
