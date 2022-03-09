package me.bluetree242.jdaeventer.impl;

import lombok.Getter;
import me.bluetree242.jdaeventer.DiscordListener;
import me.bluetree242.jdaeventer.EventHandler;
import me.bluetree242.jdaeventer.HandlerPriority;
import me.bluetree242.jdaeventer.JDAEventer;
import me.bluetree242.jdaeventer.annotations.HandleEvent;
import me.bluetree242.jdaeventer.exceptions.BadListenerException;
import me.bluetree242.jdaeventer.objects.EventInformation;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.lang.reflect.Method;

/**
 * the handler for listeners, one per method
 */
public class MethodEventHandler implements EventHandler {
    private static final Logger LOG = JDALogger.getLog(MethodEventHandler.class);
    /**
     * get the event priority in the method's annotation
     *
     * @return event priority of the event handler and method
     */
    @Getter
    private final HandlerPriority priority;
    @Getter
    private final Class event;
    /**
     * get the listener for that method
     *
     * @return listener of the method
     */
    @Getter
    private final DiscordListener listener;
    /**
     * method that this instance is for, it is also invoked on event
     *
     * @return method this instance is for
     */
    @Getter
    private final Method method;

    @Getter
    private final boolean ignoreMarkCancelled;

    public MethodEventHandler(Method method, DiscordListener listener) {
        this.listener = listener;
        HandleEvent annot = method.getAnnotation(HandleEvent.class);
        if (annot == null) throw new IllegalStateException("Bad Method to register");
        if (method.getParameterCount() > 2)
            throw new BadListenerException("@HandleEvent on a method with more than 2 parameters");
        if (!GenericEvent.class.isAssignableFrom(method.getParameterTypes()[0]))
            throw new BadListenerException("Method " + method.toGenericString() + " first parameter is not an event");
        if (method.getParameterCount() == 2)
            if (!EventInformation.class.isAssignableFrom(method.getParameterTypes()[1]))
                throw new BadListenerException("Second parameter for method " + method.toGenericString() + " is not EventInformation");
        priority = annot.priority();
        ignoreMarkCancelled = annot.ignoreCancelMark();
        event = method.getParameterTypes()[0];
        this.method = method;
    }


    @Override
    public void onEvent(@NotNull GenericEvent event, EventInformation info) {
        try {
            if (method.getParameterCount() == 1)
                method.invoke(listener, event);
            else method.invoke(listener, event, info);
        } catch (Exception e) {
            LOG.error("Could not pass event " + event.getClass().getSimpleName() + " to " + listener.getClass().getName(), e);
        }
    }

}
