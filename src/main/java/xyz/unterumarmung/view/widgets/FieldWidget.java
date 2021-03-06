package xyz.unterumarmung.view.widgets;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.events.MessageData;
import xyz.unterumarmung.events.MessageListener;
import xyz.unterumarmung.events.MessageSource;
import xyz.unterumarmung.events.SubscriptionHandler;
import xyz.unterumarmung.model.GameField;
import xyz.unterumarmung.model.events.CellMessage;
import xyz.unterumarmung.view.WidgetFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.stream.Collectors;

public class FieldWidget extends JPanel {
    private final @NotNull GameField gameField;
    private final @NotNull WidgetFactory widgetFactory;

    public FieldWidget(@NotNull GameField gameField, @NotNull WidgetFactory widgetFactory, @NotNull SubscriptionHandler subscriptionHandler) {
        this.gameField = gameField;
        this.widgetFactory = widgetFactory;
        setLayout(new GridLayout(gameField.height(), gameField.width()));
        subscribeOnCells(subscriptionHandler);
        build();
    }

    private void subscribeOnCells(@NotNull SubscriptionHandler subscriptionHandler) {
        var cellController = new CellController();
        for (var cellWithPosition : gameField.cells()) {
            subscriptionHandler.subscribeTo(cellWithPosition.cell, cellController);
        }
    }

    private void build() {
        var sortedCells = gameField.cells().stream()
                .sorted(Comparator.comparingInt(cellWithPosition -> cellWithPosition.position.x))
                .sorted(Comparator.comparingInt(cellWithPosition -> cellWithPosition.position.y))
                .map(cellWithPosition -> cellWithPosition.cell)
                .collect(Collectors.toList());

        for (var cell : sortedCells) {
            var cellWidget = widgetFactory.getWidget(cell);
            add(cellWidget);
        }
    }

    private class CellController implements MessageListener {

        @Override
        public void handleMessage(MessageSource source, MessageData data) {
            if (data instanceof CellMessage) {
                var cellMessage = (CellMessage) data;
                switch (cellMessage.type) {
                    case OBJECT_ENTERED -> onObjectEntered(cellMessage);
                    case OBJECT_LEAVED -> onObjectLeaved(cellMessage);
                }
            }
        }

        private void onObjectLeaved(CellMessage cellMessage) {
            var cellWidget = widgetFactory.getWidget(cellMessage.cell);
            var objectWidget = widgetFactory.getWidget(cellMessage.object);
            if (objectWidget == null)
                return;
            cellWidget.removeObject(objectWidget);
        }

        private void onObjectEntered(CellMessage cellMessage) {
            var cellWidget = widgetFactory.getWidget(cellMessage.cell);
            var objectWidget = widgetFactory.getWidget(cellMessage.object);
            if (objectWidget == null)
                return;
            cellWidget.addObject(objectWidget);
        }
    }
}
