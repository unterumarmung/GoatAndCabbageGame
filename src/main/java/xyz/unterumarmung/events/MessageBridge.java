package xyz.unterumarmung.events;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageBridge implements MessageSender, SubscriptionHandler {
    private final Map<MessageSource, List<MessageListener>> listeners = new HashMap<>();

    @Override
    public void emitMessage(@NotNull MessageSource messageSource, MessageData data) {
        if (!listeners.containsKey(messageSource))
            return;
        for (var listener : listeners.get(messageSource)) {
            listener.handleMessage(messageSource, data);
        }
    }

    @Override
    public void subscribeTo(MessageSource messageSource, MessageListener messageListener) {
        ensureListForMessageSource(messageSource);
        listeners.get(messageSource).add(messageListener);
    }

    private void ensureListForMessageSource(MessageSource source) {
        listeners.computeIfAbsent(source, k -> new ArrayList<>());
    }
}
