package me.bluetree242.jdaeventer.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * thrown when you try to register a listener containing problems
 */
@AllArgsConstructor
public class BadListenerException extends RuntimeException {
    @Getter
    private final String message;
}
