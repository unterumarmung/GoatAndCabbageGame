package xyz.unterumarmung.model.events;

import xyz.unterumarmung.events.MessageData;
import xyz.unterumarmung.model.Cell;

import java.util.Objects;

public class GoatMessage extends MessageData {
    public final Cell from;
    public final Cell to;

    public GoatMessage(Cell from, Cell to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoatMessage that = (GoatMessage) o;
        return (that.from == this.from && that.to == this.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
