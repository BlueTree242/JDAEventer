package me.bluetree242.eventer.annotations;

import me.bluetree242.eventer.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubscribeEvent {

    EventPriority priority() default EventPriority.NORMAL;
}
