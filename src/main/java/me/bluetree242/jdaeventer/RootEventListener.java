package me.bluetree242.jdaeventer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is the listener which calls all the handlers, you can get the one from {@link JDAEventer#getRootListener()}
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RootEventListener implements EventListener {
    /**
     * gets the eventer instance for this root listener
     * @see JDAEventer#getRootListener()
     */
    @Getter @NotNull
    private final JDAEventer eventer;

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        Set<EventHandler> handlers = new HashSet<>(eventer.getHandlers());
        handlers.stream().sorted(Comparator.comparingInt(o -> o.getPriority().getAsNum()));
        for (EventHandler handler : handlers) {
            if (handler.getEvent().isInstance(event))
            handler.onEvent(event);
        }
    }
}
