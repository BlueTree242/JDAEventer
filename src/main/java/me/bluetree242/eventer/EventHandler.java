package me.bluetree242.eventer;

import net.dv8tion.jda.api.events.GenericEvent;

public interface EventHandler<T extends GenericEvent> {

    void onEvent(T event);

    EventPriority getPriority();

    Class<T> getEvent();
}
