package xyz.unterumarmung.events;

// Приниматор сообщений
public interface MessageListener {
    void handleMessage(MessageSource source, MessageData data);
}
