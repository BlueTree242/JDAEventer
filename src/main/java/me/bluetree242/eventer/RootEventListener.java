package me.bluetree242.eventer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@AllArgsConstructor
public class RootEventListener implements EventListener {
    @Getter private final Eventer core;
    @Override
    public void onEvent(@NotNull GenericEvent event) {
        Set<EventHandler> handlers = new HashSet<>(core.getHandlers());
        handlers.stream().sorted(new Comparator<EventHandler>() {
            @Override
            public int compare(EventHandler o1, EventHandler o2) {
                return o1.getPriority().getAsNum() - o2.getPriority().getAsNum();
            }
        });
        for (EventHandler handler : handlers) {
            handler.onEvent(event);
        }
    }
}
