package me.bluetree242.jdaeventer;

import net.dv8tion.jda.api.events.GenericEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class is event handler, you can add event handlers to {@link JDAEventer#addEventHandler(EventHandler)} and it will call the {@link EventHandler#onEvent(GenericEvent)} if the events match <br>
 * Example Event Handler is the {@link me.bluetree242.jdaeventer.impl.MethodEventHandler} which is one per method when you add a {@link DiscordListener}
 * @see JDAEventer#addEventHandler(EventHandler)
 * @param <T> event type
 */
public interface EventHandler<T extends GenericEvent> {

    /**
     * This method is called when there is an event and it matches the event class
     * @param event event to handle
     */
    void onEvent(@NotNull T event);

    @NotNull HandlerPriority getPriority();

    /**
     * event this handler is for, if it matches (or instanceof is true) event {@link EventHandler#onEvent(GenericEvent) will be called}
     * @return
     */
    @NotNull Class<T> getEvent();
}
