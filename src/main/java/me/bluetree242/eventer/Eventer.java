package me.bluetree242.eventer;

import lombok.Getter;
import lombok.Setter;
import me.bluetree242.eventer.annotations.SubscribeEvent;
import me.bluetree242.eventer.impl.MethodEventHandler;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class Eventer {
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

    public Eventer() {
        rootListener = new RootEventListener(this);
    }

    /**
     * Add a listener to this eventer instance
     * @param listener listener to add
     */
    public void addListener(DiscordListener listener) {
        try {
            for (Method method : listener.getClass().getMethods()) {
                if (method.getAnnotation(SubscribeEvent.class) != null)
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
     * @see Eventer#removeListener(Class)
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
     * @see Eventer#removeListener(DiscordListener)
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
