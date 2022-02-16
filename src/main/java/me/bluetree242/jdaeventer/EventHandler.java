package me.bluetree242.jdaeventer;

import me.bluetree242.jdaeventer.objects.EventInformation;
import net.dv8tion.jda.api.events.GenericEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class is event handler, you can add event handlers to {@link JDAEventer#addEventHandler(EventHandler)} and it will call the {@link EventHandler#onEvent(GenericEvent, EventInformation)} if the events match <br>
 * Example Event Handler is the {@link me.bluetree242.jdaeventer.impl.MethodEventHandler} which is one per method when you add a {@link DiscordListener}
 *
 * @param <T> event type
 * @see JDAEventer#addEventHandler(EventHandler)
 */
public interface EventHandler<T extends GenericEvent> {

    /**
     * This method is called when there is an event and it matches the event class
     *
     * @param event event to handle
     * @param info
     */
    void onEvent(@NotNull T event, EventInformation info);

    @NotNull HandlerPriority getPriority();

    /**
     * event this handler is for, if it matches (or instanceof is true) event {@link EventHandler#onEvent(GenericEvent, EventInformation) will be called}
     *
     * @return
     */
    @NotNull Class<T> getEvent();
}
