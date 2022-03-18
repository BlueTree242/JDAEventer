package me.bluetree242.jdaeventer.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.bluetree242.jdaeventer.JDAEventer;
import net.dv8tion.jda.api.events.GenericEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * This is information about an event.
 */
@RequiredArgsConstructor
public class EventInformation {
    /**
     * Get the notes added here
     *
     * @return all notes
     */
    @Getter
    private final HashMap<String, Object> notes = new HashMap<>();
    /**
     * get the eventer instance
     *
     * @return eventer instance
     */
    @Getter
    private final JDAEventer eventer;
    private final GenericEvent originEvent;

    /**
     * Mark this event as cancelled for other handlers to know.
     * This is when you delete a message in a {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent}
     * and queued deletion of the message for example.
     *
     * @param markedCancelled boolean to set
     * @see EventInformation#isMarkedCancelled()
     */
    @Setter
    private boolean markedCancelled = false;

    /**
     * An event should only be marked as cancelled if undoing of action (like deleting in message event) was queued
     *
     * @return true if marked as cancelled
     * @see EventInformation#setMarkedCancelled(boolean)
     */
    public boolean isMarkedCancelled() {
        return markedCancelled;
    }
    private Connection connection = null;

    /**
     * Get the database connection
     * @return the database connection, if the  connection isn't open it opens a new one
     * @throws UnsupportedOperationException if the connection supplier isn't set
     * @see JDAEventer#setConnectionSupplier(Supplier)
     */
    public Connection getConnection() {
        if (!isConnectionOpen()) {
            if (eventer.getConnectionSupplier() == null) throw new UnsupportedOperationException("Connection Supplier is not set");
            return connection = eventer.getConnectionSupplier().get();
        } else return connection;
    }

    /**
     * Check if the Connection with database is open
     * @return true if database connection is open, false otherwise
     */
    public boolean isConnectionOpen() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Add a note to this event
     * Overrides old one if already present
     *
     * @param name  name of note
     * @param value value for the note, can be any object
     */
    public void addNote(@NotNull String name, @NotNull Object value) {
        notes.put(name, value);
    }

    /**
     * Removes the note from notes
     *
     * @param name name of note
     */
    public void removeNote(@NotNull String name) {
        notes.remove(name);
    }

    /**
     * Get a specific note from the event
     *
     * @param name name of the note
     * @return the note if it exists, null otherwise
     */
    @Nullable
    public Object getNote(@NotNull String name) {
        return notes.get(name);
    }

    /**
     * Get a note as a string
     *
     * @param name name of note
     * @return null if it note doesn't exist or it is not a String, else the string note
     */
    @Nullable
    public String getNoteString(@NotNull String name) {
        if ((getNote(name) instanceof String)) return (String) getNote(name);
        return null;
    }

    /**
     * Get a note as a boolean
     *
     * @param name name of note
     * @return null if it note doesn't exist or it is not a boolean, else the boolean note
     */
    @Nullable
    public Boolean getNoteBoolean(@NotNull String name) {
        if ((getNote(name) instanceof Boolean)) return (Boolean) getNote(name);
        return null;
    }

    /**
     * returns the new event set
     * @return the new set event
     * @see EventInformation#setEvent(GenericEvent)
     */
    @Getter private GenericEvent newEvent;

    /**
     * Set this event as a new event, the event MUST extend the original event,
     * this is useful when you want to add more future handlers in a good way, and keeping the handlers in the future that were already
     * going to get called
     * @param event Event Object to set
     * @throws IllegalArgumentException if event does not extend the original event
     */
    public void setEvent(@NotNull GenericEvent event) {
        if (!originEvent.getClass().isInstance(event)) {
            throw new IllegalArgumentException("Event (" + event.getClass().getName() +") must extend the original event (" + originEvent.getClass().getSimpleName() + ")");
        }
        newEvent = event;
    }

}
