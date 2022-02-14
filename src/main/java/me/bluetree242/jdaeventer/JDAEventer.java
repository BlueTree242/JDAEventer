package me.bluetree242.jdaeventer;

import lombok.Getter;
import me.bluetree242.jdaeventer.annotations.HandleEvent;
import me.bluetree242.jdaeventer.impl.MethodEventHandler;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class JDAEventer {
    @Getter
    private static final Set<EventHandler> handlers = new HashSet<>();
    public static Set<Class<? extends GenericEvent>> events = null;

    static {
        events = new HashSet<>();
        Class clazz = ListenerAdapter.class;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getParameterCount() == 1) {
                if (GenericEvent.class.isAssignableFrom(method.getParameters()[0].getType()))
                    events.add((Class<? extends GenericEvent>) method.getParameters()[0].getType());
            }
        }
    }


    private final RootEventListener rootListener;

    /**
     * gets the root instance, which is the listener you should add to your jda, this one listen for events and calls all the handlers
     * @return the root listener for the eventer instance
     * @see net.dv8tion.jda.api.JDA#addEventListener(Object...)
     */
    public RootEventListener getRootListener() {
        return rootListener;
    }

    public JDAEventer() {
        rootListener = new RootEventListener(this);
    }

    /**
     * Add a listener to this eventer instance
     * @param listener listener to add
     */
    public void addListener(DiscordListener listener) {
        try {
            for (Method method : listener.getClass().getMethods()) {
                if (method.getAnnotation(HandleEvent.class) != null)
                    handlers.add((new MethodEventHandler(method, listener)));
            }
        } catch (Exception x) {
            removeListener(listener);
            throw x;
        }
    }

    /**
     * remove a listener, must be the exact instance registered before
     * @param listener listener to remove
     * @see JDAEventer#removeListener(Class)
     */
    public void removeListener(DiscordListener listener) {
        for (EventHandler h : new HashSet<>(handlers)) {
            if (h instanceof MethodEventHandler) {
                MethodEventHandler handler = (MethodEventHandler) h;
                if (handler.getListener() == listener) handlers.remove(h);
            }
        }
    }

    /**
     * removes any handler registered from this listener class
     * @param listener listener class to remove
     * @see JDAEventer#removeListener(DiscordListener)
     */
    public void removeListener(Class<? extends DiscordListener> listener) {
        for (EventHandler h : new HashSet<>(handlers)) {
            if (h instanceof MethodEventHandler) {
                MethodEventHandler handler = (MethodEventHandler) h;
                if (listener.isInstance(handler.getListener())) handlers.remove(h);
            }
        }
    }


}
