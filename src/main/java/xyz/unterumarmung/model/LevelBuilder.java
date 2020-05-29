package xyz.unterumarmung.model;

import xyz.unterumarmung.events.MessageBridge;

public class LevelBuilder {
    public Game buildSimpleGame() {
        var messageBridge = new MessageBridge();
        return new Game(new SimpleFieldFactory(messageBridge), messageBridge, messageBridge);
    }
}
