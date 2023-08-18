package com.foxandr.telegrambot.util;

import org.telegram.telegrambots.meta.api.objects.Chat;

public final class ChatUtils {
    private ChatUtils(){}

    public static String getGroupChatOrUserName(Chat chat){
        return isGroupChat(chat) ? "Group chat: "+chat.getTitle() : "Private message";
    }

    public static boolean isGroupChat(Chat chat){
        return chat.isSuperGroupChat() || chat.isGroupChat();
    }
}
