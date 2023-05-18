# ⚠ Stable but not complete

# What is JDAEventer?

JDAEventer is a JDA Library for a better event system, currently pretty small but many features still coming.

JDAEventer aims to manage your events better than regular ListenerAdapter

# Features

- No more reason to use specific method named method for event to work
- priorities for events
- better management for all your listeners (well not really possible now)
- Very easy to use, if you already use regular jda listeners switching isn't hard
- Remove all listeners related to a listener class using its class
- Add some information to an event for other listeners to know about
- Fire Custom Events!

# How to integrate to your project

The library is in alpha. Until now there is no major release, or major alpha release, you can get the latest snapshot version from 
[here](https://repo.bluetree242.ml/api/maven/latest/version/maven-snapshots/me/bluetree242/jdaeventer/JDAEventer) or for a jar file use [CI](https://ci.bluetree242.tk/job/JDAEventer)

## Maven

Add the repository

```xml
<repositories>
  <repository>
    <id>bluetree242-repo</id>
    <url>https://repo.bluetree242.ml/maven-public/</url>
  </repository>
</repositories>
```

And the dependency

```xml
<dependency>
  <groupId>com.github.BlueTree242</groupId>
  <artifactId>JDAEventer</artifactId>
  <version>VERSION</version>
</dependency>
```

## Gradle

Add the repository

```groovy
repositories {
    maven { url 'https://repo.bluetree242.ml/repository/maven-public/' }
}
```

And the dependency

```groovy
dependencies {
    implementation 'com.github.BlueTree242:JDAEventer:VERSION'
}
```

# Javadoc

Javadoc can be found [here](https://ci.bluetree242.tk/job/JDAEventer/javadoc/index.html)

# How to use

```java
public class Bot {
    public static void main(String[] args) {
        JDAEventer eventer = new JDAEventer();
        JDA jda; //init your jda instance here
        jda.addEventListener(eventer.getRootListener()); //if you want add this in the builder of jda
        //now forget about registering events from jda, register them in eventer!
        eventer.addListener(new MessageListener());
    }

}

public class MessageListener implements DiscordListener {

    @HandleEvent //default priority is NORMAL
    public void onMessage(MessageReceivedEvent e, EventInformation info) { //method name doesn't matter anymore, info is optional
        info.addNote("wasListened", true);
        //some code
    }

    @HandleEvent(priority = HandlerPriority.MONITOR) //monitor means your listener will be the last to be called, you shouldn't delete message here 
    public void onLastListenMessage(MessageReceivedEvent e, EventInformation info) { //info is optional, if you don't need it then don't add it
        System.out.println(info.getNoteBoolean("wasListened")); //should print true
    }

    //onMessage() will be called before onLastListenMessage()! This guarantee that wasListened is true unless another third handler removes it.
}
```

# Recommendations

- Don't delete message (or remove reaction in reaction event etc) if its MONITOR, if you wish you should use HIGHEST
  priority

# Planned Features

- ~~Marking events as cancelled~~ Added
- ~~Adding event notes, for other listeners to read and know some information (for example let other listeners know if
  this message isn't regular and is a command)~~ Added
- Schedule an event to run for temporary time, and removes itself (or manually but easy)
- Limit a listener to a specific guild, in 1 line of code!
- And MANY MORE!
