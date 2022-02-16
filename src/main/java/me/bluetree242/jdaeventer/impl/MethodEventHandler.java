package me.bluetree242.jdaeventer.impl;

import lombok.Getter;
import me.bluetree242.jdaeventer.*;
import me.bluetree242.jdaeventer.annotations.HandleEvent;
import me.bluetree242.jdaeventer.exceptions.BadListenerException;
import net.dv8tion.jda.api.events.GenericEvent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * the handler for listeners, one per method
 */
public class MethodEventHandler implements EventHandler {
    /**
     * get the event priority in the method's annotation
     * @return event priority of the event handler and method
     */
    @Getter
    private final HandlerPriority priority;
    @Getter
    private final Class event;
    /**
     * get the listener for that method
     * @return listener of the method
     */
    @Getter
    private final DiscordListener listener;
    /**
     * method that this instance is for, it is also invoked on event
     * @return method this instance is for
     */
    @Getter
    private final Method method;

    public MethodEventHandler(Method method, DiscordListener listener) {
        this.listener = listener;
        HandleEvent annot = method.getAnnotation(HandleEvent.class);
        if (annot == null) throw new IllegalStateException("Bad Method to register");
        if (method.getParameterCount() != 1)
            throw new BadListenerException("@SubscribeEvent on a method with more than/less than 1 parameter");
        if (!JDAEventer.events.contains(method.getParameterTypes()[0]))
            throw new BadListenerException("Method " + method.toGenericString() + " first parameter is not an event");
        priority = annot.priority();
        event = method.getParameterTypes()[0];
        this.method = method;
    }


    @Override
    public void onEvent(@NotNull GenericEvent event) {
        try {
            method.invoke(listener, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
