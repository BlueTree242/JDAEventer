package me.bluetree242.eventer;

public enum EventPriority {


    LOWEST(0),

    LOW(1),

    NORMAL(2),

    HIGH(3),

    HIGHEST(4),

    MONITOR(5);

    private final int asNum;

    private EventPriority(int num) {
        this.asNum = num;
    }

    public int getAsNum() {
        return asNum;
    }
}
