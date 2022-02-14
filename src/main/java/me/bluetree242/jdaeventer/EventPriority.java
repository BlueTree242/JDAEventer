package me.bluetree242.jdaeventer;

public enum EventPriority {

    /**
     * The handler method must be the first to be called, no guarantee to be the exact first if other handlers use this priority
     */
    LOWEST(0),

    /**
     * Must be one of the first to handle the event, but some listeners can go first (LOWEST ones)
     */
    LOW(1),

    /**
     * The Default Priority. Indicates that the handler is used in middle of other handlers
     */
    NORMAL(2),

    /**
     * higher than the default (NORMAL) and directly after it
     */
    HIGH(3),

    /**
     * The handler MUST be the last to handle the event
     */
    HIGHEST(4),

    /**
     * Run after all previous handlers handled the event, you shouldn't undo actions (like deleting message on message event) here, use HIGHEST instead
     */
    MONITOR(5);

    private final int asNum;

    private EventPriority(int num) {
        this.asNum = num;
    }

    public int getAsNum() {
        return asNum;
    }
}
