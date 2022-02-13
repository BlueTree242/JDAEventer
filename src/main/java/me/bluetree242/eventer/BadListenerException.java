package me.bluetree242.eventer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/**
 * thrown when you try to register a listener containing problems
 */
public class BadListenerException extends RuntimeException {
    @Getter
    private final String msg;
}
