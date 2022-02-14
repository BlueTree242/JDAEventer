package me.bluetree242.jdaeventer.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * thrown when you try to add a listener containing problems
 * @see me.bluetree242.jdaeventer.JDAEventer#addListener(DiscordListener)
 */
@AllArgsConstructor
public class BadListenerException extends RuntimeException {
    @Getter
    private final String message;
}
