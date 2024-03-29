package me.bluetree242.jdaeventer;

import lombok.Getter;
import me.bluetree242.jdaeventer.annotations.CustomEvent;
import me.bluetree242.jdaeventer.annotations.HandleEvent;
import me.bluetree242.jdaeventer.impl.MethodEventHandler;
import me.bluetree242.jdaeventer.objects.EventInformation;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JDAEventer {
    /**
     * get the events that exist in jda, the events here are unmodifiable
     *
     * @return all events that exist in jda
     */
    @Getter
    private static final Set<Class<? extends GenericEvent>> events;
    private static final Logger LOG = JDALogger.getLog(MethodEventHandler.class);

    static {
        Set<Class<? extends GenericEvent>> jdaEvents = new HashSet<>();
        Class clazz = ListenerAdapter.class;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getParameterCount() == 1) {
                if (GenericEvent.class.isAssignableFrom(method.getParameters()[0].getType()))
                    //noinspection unchecked
                    jdaEvents.add((Class<? extends GenericEvent>) method.getParameters()[0].getType());
            }
        }
        events = Collections.unmodifiableSet(jdaEvents);
    }

    /**
     * get the handlers added to this eventer instance
     *
     * @return set of handlers added to this instance
     */
    @Getter
    private final Set<EventHandler> handlers = ConcurrentHashMap.newKeySet();
    /**
     * gets the root instance, which is the listener you should add to your jda, this one listen for events and calls all the handlers
     *
     * @return the root listener for the eventer instance
     * @see net.dv8tion.jda.api.JDA#addEventListener(Object...)
     */
    @Getter(onMethod_ = {@NotNull})
    private final RootEventListener rootListener;


    public JDAEventer() {
        rootListener = new RootEventListener(this);
    }

    /**
     * Add a listener to this eventer instance.
     * This method adds a {@link MethodEventHandler} for every method with annotation {@link HandleEvent}
     *
     * @param listener listener to add
     * @return this JDAEventer, for a chain
     * @throws me.bluetree242.jdaeventer.exceptions.BadListenerException if listener has a problem
     */
    public JDAEventer addListener(@NotNull DiscordListener listener) {
        try {
            for (Method method : listener.getClass().getMethods()) {
                if (method.getAnnotation(HandleEvent.class) != null)
                    handlers.add((new MethodEventHandler(method, listener)));
            }
        } catch (Exception x) {
            removeListener(listener);
            throw x;
        }
        return this;
    }

    /**
     * remove a listener, must be the exact instance added before.
     * This method removes any {@link MethodEventHandler} handler registered from this listener
     *
     * @param listener listener to remove
     * @return this JDAEventer, for a chain
     * @see JDAEventer#removeListener(Class)
     */
    public JDAEventer removeListener(@NotNull DiscordListener listener) {
        for (EventHandler h : new HashSet<>(handlers)) {
            if (h instanceof MethodEventHandler) {
                MethodEventHandler handler = (MethodEventHandler) h;
                if (handler.getListener() == listener) handlers.remove(h);
            }
        }
        return this;
    }

    /**
     * adds an event handler to list of handlers
     *
     * @param handler add a handler to list of handlers
     * @return this JDAEventer, for a chain
     * @see EventHandler
     */
    public JDAEventer addEventHandler(@NotNull EventHandler handler) {
        handlers.add(handler);
        return this;
    }

    /**
     * removes a handler from handler list
     *
     * @param handler handler to remove
     * @return this JDAEventer, for a chain
     */
    public JDAEventer removeEventHandler(@NotNull EventHandler handler) {
        handlers.remove(handler);
        return this;
    }

    /**
     * removes any handler added from this listener class.
     * This method removes any {@link MethodEventHandler} handler registered from this class
     *
     * @param listener listener class to remove
     * @return this JDAEventer, for a chain
     * @see JDAEventer#removeListener(DiscordListener)
     */
    public JDAEventer removeListener(@NotNull Class<? extends DiscordListener> listener) {
        for (EventHandler h : new HashSet<>(handlers)) {
            if (h instanceof MethodEventHandler) {
                MethodEventHandler handler = (MethodEventHandler) h;
                if (listener.isInstance(handler.getListener())) handlers.remove(h);
            }
        }
        return this;
    }

    /**
     * Fire any event, {@link RootEventListener} calls this when there is an event
     *
     * @param event event to fire
     * @param info  Event Information to pass to all handlers
     * @throws IllegalArgumentException if the event is not custom or jda event
     */
    public void fireEvent(@NotNull Object event, @NotNull EventInformation info) {
        if (!(event instanceof Event) && event.getClass().getAnnotation(CustomEvent.class) == null)
            throw new IllegalArgumentException("This event is not custom or a jda event");
        Set<EventHandler> handlers = new HashSet<>(getHandlers());
        handlers = handlers.stream().sorted(Comparator.comparingInt(o -> o.getPriority().getAsNum())).collect(Collectors.toCollection(LinkedHashSet::new));
        for (EventHandler handler : handlers) {
            if (info.isMarkedCancelled() && handler.isIgnoreMarkCancelled()) continue; //ignore
            if (handler.getEvent().isInstance(info.getNewEvent() == null ? event : info.getNewEvent()))
                try {
                    //noinspection unchecked
                    handler.onEvent(info.getNewEvent() == null ? event : info.getNewEvent(), info);
                } catch (Exception ex) {
                    LOG.error("One of the EventHandlers had an uncaught exception", ex);
                }
        }
    }

    /**
     * Fire an event, calls {@link JDAEventer#fireEvent(Object, EventInformation)} with a new {@link EventInformation}
     *
     * @param event event to fire
     */
    public void fireEvent(@NotNull Object event) {
        fireEvent(event, new EventInformation(this, event));
    }
}
