package xyz.unterumarmung;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.events.MessageSender;
import xyz.unterumarmung.events.SubscriptionHandler;
import xyz.unterumarmung.model.FieldFactory;
import xyz.unterumarmung.model.Game;

public class Level {
    public final int id;
    public final String description;
    private final @NotNull MessageSender messageSender;
    private final @NotNull SubscriptionHandler subscriptionHandler;
    private final @NotNull FieldFactory fieldFactory;

    public Level(int id, String description, @NotNull MessageSender messageSender, @NotNull SubscriptionHandler subscriptionHandler, @NotNull FieldFactory fieldFactory) {
        this.id = id;
        this.description = description;
        this.messageSender = messageSender;
        this.subscriptionHandler = subscriptionHandler;
        this.fieldFactory = fieldFactory;
    }

    public Game game() {
        return new Game(fieldFactory, subscriptionHandler, messageSender);
    }

}
