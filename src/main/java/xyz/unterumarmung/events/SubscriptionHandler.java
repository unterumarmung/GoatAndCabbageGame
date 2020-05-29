package xyz.unterumarmung.events;

public interface SubscriptionHandler {
    void subscribeTo(MessageSource messageSource, MessageListener messageListener);
}
