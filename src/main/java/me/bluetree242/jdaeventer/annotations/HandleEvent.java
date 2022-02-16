package me.bluetree242.jdaeventer.annotations;

import me.bluetree242.jdaeventer.DiscordListener;
import me.bluetree242.jdaeventer.HandlerPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Put this on methods to add method as {@link me.bluetree242.jdaeventer.impl.MethodEventHandler}, if you do not include this on method it will be ignored when you add
 * @see me.bluetree242.jdaeventer.JDAEventer#addListener(DiscordListener)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleEvent {

    /**
     * A priority determines when to call your method
     * @return priority of the method
     */
     HandlerPriority priority() default HandlerPriority.NORMAL;
}
