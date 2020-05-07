package events;

import org.jetbrains.annotations.NotNull;

// Будет передаваться в конструктор экземляра MessageSource
public interface MessageSender {
    void emitMessage(@NotNull MessageSource messageSource, MessageData data);
}
