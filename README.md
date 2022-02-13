# ⚠ Stable but not complete

# What is Eventer?

Eventer is a JDA Library for a better event system, currently pretty small but many features still coming.

Eventer aims to manage your events better than regular ListenerAdapter

# Features

- No more reason to use specific method named method for event to work
- priorities for events
- better management for all your listeners (well not really possible now)
- Very easy to use, if you already use regular jda listeners switching isn't hard
- Remove all listeners related to a listener class using its class

# How to use

```java
public class Bot {
    public static void main(String[] args) {
        Eventer eventer = new Eventer();
        JDA jda; //init your jda instance here
        jda.addEventListener(eventer.getRootListener()); //if you want add this in the builder of jda
        //now forget about registering events from jda, register them in eventer!
        eventer.addListener(new MessageListener());
    }

}

public class MessageListener implements DiscordListener {

    @SubscribeEvent //default priority is NORMAL
    public void onMessage(MessageReceivedEvent e) { //method name doesn't matter anymore
        //now listen for your event whatever you want to do
    }

    @SubscribeEvent(priority = EventPriority.MONITOR)
    public void onLastListenMessage(MessageReceivedEvent e) {
        //monitor means your listener will be the last to be called, you shouldn't delete message here 
    }

    //onMessage() will be called before onLastListenMessage()!
}
```
TAKE CARE! Make sure you use the annotation from the library and not jda (check your imports)

# Recommendations

- Don't delete message (or remove reaction in reaction event etc) if its MONITOR, if you wish you should use HIGHEST
  priority

# Planned Features

- Marking events as cancelled
- Adding event notes, for other listeners to read and know some information (for example let other listeners know if
  this message isn't regular and is a command)
- Schedule an event to run for temporary time, and removes itself (or manually but easy)
- Limit a listener to a specific guild, in 1 line of code!
- And MANY MORE!



