package me.bluetree242.jdaeventer.annotations;

import me.bluetree242.jdaeventer.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * Put this on methods to subscribe to, if you do not include this on method it will be ignored when you register
 */
public @interface HandleEvent {

    /**
     * A priority determines when to call your method
     * @return priority of the method
     */
    EventPriority priority() default EventPriority.NORMAL;
}
