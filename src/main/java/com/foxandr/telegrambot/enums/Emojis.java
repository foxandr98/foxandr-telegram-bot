package com.foxandr.telegrambot.enums;

public enum Emojis {
    BEER("\uD83C\uDF7A"),
    CLINKING_GLASS("\uD83E\uDD42"),
    FOX("\uD83E\uDD8A"),
    DICE("\uD83C\uDFB2");
    private String value;

    Emojis(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
