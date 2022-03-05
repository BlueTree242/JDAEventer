package me.bluetree242.jdaeventer;

import lombok.Getter;
import me.bluetree242.jdaeventer.annotations.HandleEvent;
import me.bluetree242.jdaeventer.impl.MethodEventHandler;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class JDAEventer {
    /**
     * get the events that exist in jda, you can add events to this
     *
     * @return all events that exist in jda
     */
    @Getter
    private static final Set<Class<? extends GenericEvent>> events;

    /**
     * Get the supplier used to get database connections
     * @return the connection supplier if set, false otherwise
     * @see JDAEventer#setConnectionSupplier(Supplier)
     * @see me.bluetree242.jdaeventer.objects.EventInformation#getConnection()
     */
    @Getter
    private Supplier<Connection> connectionSupplier = null;

    static {
        events = new HashSet<>();
        Class clazz = ListenerAdapter.class;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getParameterCount() == 1) {
                if (GenericEvent.class.isAssignableFrom(method.getParameters()[0].getType()))
                    //noinspection unchecked
                    events.add((Class<? extends GenericEvent>) method.getParameters()[0].getType());
            }
        }
    }

    /**
     * get the handlers added to this eventer instance
     *
     * @return set of handlers added to this instance
     */
    @Getter
    private final Set<EventHandler> handlers = new HashSet<>();
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
     * Set the connection provider
     * @param provider the provider to use when you want to open a connection
     * @see me.bluetree242.jdaeventer.objects.EventInformation#getConnection()
     */
    public void setConnectionSupplier(Supplier<Connection> provider) {
        connectionSupplier = provider;
    }

}
