package model;

import events.MessageBridge;

public class LevelBuilder {
    Game buildSimpleGame() {
        var messageBridge = new MessageBridge();
        return new Game(new SimpleFieldFactory(messageBridge), messageBridge, messageBridge);
    }
}
