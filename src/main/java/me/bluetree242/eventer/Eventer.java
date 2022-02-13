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


    @Getter
    @Setter
    private EventListener rootListener;

    public Eventer() {
        rootListener = new RootEventListener(this);
    }

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

    public void removeListener(DiscordListener listener) {
        for (EventHandler h : new HashSet<>(handlers)) {
            if (h instanceof MethodEventHandler) {
                MethodEventHandler handler = (MethodEventHandler) h;
                if (handler.getListener() == listener) handlers.remove(h);
            }
        }
    }

    public void removeListener(Class<? extends DiscordListener> listener) {
        for (EventHandler h : new HashSet<>(handlers)) {
            if (h instanceof MethodEventHandler) {
                MethodEventHandler handler = (MethodEventHandler) h;
                if (listener.isInstance(handler.getListener())) handlers.remove(h);
            }
        }
    }


}
