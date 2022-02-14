package me.bluetree242.jdaeventer.impl;

import lombok.Getter;
import me.bluetree242.jdaeventer.*;
import me.bluetree242.jdaeventer.annotations.HandleEvent;
import net.dv8tion.jda.api.events.GenericEvent;

import java.lang.reflect.Method;

/**
 * the handler for listeners, one per method
 */
public class MethodEventHandler implements EventHandler {
    @Getter
    private final EventPriority priority;
    @Getter
    private final Class event;
    @Getter
    private final DiscordListener listener;
    @Getter
    private final Method method;

    public MethodEventHandler(Method method, DiscordListener listener) {
        this.listener = listener;
        HandleEvent annot = method.getAnnotation(HandleEvent.class);
        if (annot == null) throw new IllegalStateException("Bad Method to register");
        if (method.getParameterCount() != 1)
            throw new BadListenerException("@SubscribeEvent on a method with more than/less than 1 parameter");
        if (!JDAEventer.events.contains(method.getParameterTypes()[0]))
            throw new IllegalStateException("Method " + method.toGenericString() + " first param is not an event");
        priority = annot.priority();
        event = method.getParameterTypes()[0];
        this.method = method;
    }


    @Override
    public void onEvent(GenericEvent event) {
        try {
            method.invoke(listener, event);
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
