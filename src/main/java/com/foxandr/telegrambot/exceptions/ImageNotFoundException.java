package com.foxandr.telegrambot.exceptions;

public class ImageNotFoundException extends Exception {
    public ImageNotFoundException(String query) {
        super(String.format("Query [%s] is not found", query));
    }

}
