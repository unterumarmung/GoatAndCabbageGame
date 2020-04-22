package events;

// Приниматор сообщений
public interface MessageListener {
    void handleMessage(MessageSource source, MessageData data);
}
