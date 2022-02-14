package me.bluetree242.jdaeventer;

import lombok.Getter;
import me.bluetree242.jdaeventer.annotations.HandleEvent;
import me.bluetree242.jdaeventer.impl.MethodEventHandler;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class JDAEventer {
    /**
     * get the handlers added to this eventer instance
     * @return set of handlers added to this instance
     */
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


    /**
     * gets the root instance, which is the listener you should add to your jda, this one listen for events and calls all the handlers
     * @return the root listener for the eventer instance
     * @see net.dv8tion.jda.api.JDA#addEventListener(Object...)
     */
    @Getter(onMethod_={@NotNull})
    private final RootEventListener rootListener;



    public JDAEventer() {
        rootListener = new RootEventListener(this);
    }

    /**
     * Add a listener to this eventer instance.
     * This method adds a {@link MethodEventHandler} for every method with annotation {@link HandleEvent}
     * @throws me.bluetree242.jdaeventer.exceptions.BadListenerException if listener has a problem
     * @param listener listener to add
     */
    public void addListener(@NotNull DiscordListener listener) {
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
     * remove a listener, must be the exact instance added before.
     * This method removes any {@link MethodEventHandler} handler registered from this listener
     * @param listener listener to remove
     * @see JDAEventer#removeListener(Class)
     */
    public void removeListener(@NotNull DiscordListener listener) {
        for (EventHandler h : new HashSet<>(handlers)) {
            if (h instanceof MethodEventHandler) {
                MethodEventHandler handler = (MethodEventHandler) h;
                if (handler.getListener() == listener) handlers.remove(h);
            }
        }
    }

    /**
     * adds an event handler to list of handlers
     * @param handler add a handler to list of handlers
     * @see EventHandler
     */
    public void addEventHandler(@NotNull EventHandler handler) {
        handlers.add(handler);
    }

    /**
     * removes a handler from handler list
     * @param handler handler to remove
     */
    public void removeEventHandler(@NotNull EventHandler handler) {
        handlers.remove(handler);
    }

    /**
     * removes any handler added from this listener class.
     * This method removes any {@link MethodEventHandler} handler registered from this class
     * @param listener listener class to remove
     * @see JDAEventer#removeListener(DiscordListener)
     */
    public void removeListener(@NotNull Class<? extends DiscordListener> listener) {
        for (EventHandler h : new HashSet<>(handlers)) {
            if (h instanceof MethodEventHandler) {
                MethodEventHandler handler = (MethodEventHandler) h;
                if (listener.isInstance(handler.getListener())) handlers.remove(h);
            }
        }
    }


}
