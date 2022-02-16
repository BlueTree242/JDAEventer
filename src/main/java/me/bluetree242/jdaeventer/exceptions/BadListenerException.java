package me.bluetree242.jdaeventer.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.bluetree242.jdaeventer.DiscordListener;

/**
 * thrown when you try to add a listener containing problems
 *
 * @see me.bluetree242.jdaeventer.JDAEventer#addListener(DiscordListener)
 */
@AllArgsConstructor
public class BadListenerException extends RuntimeException {
    @Getter
    private final String message;
}
