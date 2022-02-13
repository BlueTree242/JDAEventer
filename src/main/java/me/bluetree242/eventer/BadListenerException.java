package me.bluetree242.eventer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BadListenerException extends RuntimeException {
    @Getter
    private final String msg;
}
