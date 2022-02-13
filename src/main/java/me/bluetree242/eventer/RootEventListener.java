package me.bluetree242.eventer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
/**
 * This class is the listener which calls all the handlers, you can get the one from {@link Eventer#getRootListener()}
 */
public class RootEventListener implements EventListener {
    @Getter
    private final Eventer core;

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        Set<EventHandler> handlers = new HashSet<>(core.getHandlers());
        handlers.stream().sorted(Comparator.comparingInt(o -> o.getPriority().getAsNum()));
        for (EventHandler handler : handlers) {
            if (handler.getEvent().isInstance(event))
            handler.onEvent(event);
        }
    }
}
