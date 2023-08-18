package com.foxandr.telegrambot.exceptions;

import com.foxandr.telegrambot.commands.Command;

public class InvalidArgsCountException extends Exception {

    public InvalidArgsCountException(String message) {
        super(message);
    }

    public InvalidArgsCountException(Command parsedCommand, int length) {
        this(String.format("Min args: %d, max args: %d, was %d",
                parsedCommand.getMinArgs(),
                parsedCommand.getMaxArgs(),
                length));
    }
}
