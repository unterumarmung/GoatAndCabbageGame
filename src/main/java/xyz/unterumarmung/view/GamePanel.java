package xyz.unterumarmung.view;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.events.MessageData;
import xyz.unterumarmung.events.MessageListener;
import xyz.unterumarmung.events.MessageSource;
import xyz.unterumarmung.events.SubscriptionHandler;
import xyz.unterumarmung.model.Game;
import xyz.unterumarmung.model.GameState;
import xyz.unterumarmung.model.events.GameMessage;
import xyz.unterumarmung.view.widgets.FieldWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import static xyz.unterumarmung.view.utils.DirectionUtils.isDirectionKeyCode;
import static xyz.unterumarmung.view.utils.DirectionUtils.keyCodeToDirection;

public class GamePanel extends JFrame implements MessageListener {
    private final @NotNull Game game;
    private final @NotNull FieldWidget fieldWidget;

    public GamePanel(@NotNull Game game, @NotNull FieldWidget fieldWidget, @NotNull SubscriptionHandler subscriptionHandler) throws HeadlessException {
        this.game = game;
        this.fieldWidget = fieldWidget;
        subscriptionHandler.subscribeTo(game, this);

        configureGui();

        subscribeToKeyboardEvents();
    }

    private void configureGui() {
        setVisible(true);
        JPanel content = (JPanel) this.getContentPane();
        content.add(this.fieldWidget);
        pack();
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        setTitle("Игра «Коза и капуста»");
    }

    private void subscribeToKeyboardEvents() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (isDirectionKeyCode(e.getKeyCode()) && e.isControlDown()) {
                    game.gameField().goat().hookBox(keyCodeToDirection(e.getKeyCode()));
                    fieldWidget.repaint();
                } else if (isDirectionKeyCode(e.getKeyCode())) {
                    game.gameField().goat().move(keyCodeToDirection(e.getKeyCode()));
                    fieldWidget.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    game.gameField().goat().unhookBox();
                    fieldWidget.repaint();
                }
                handleState(game.gameState());
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void handleState(GameState gameState) {
        final String MESSAGE_TITLE = "Игра завершена!";
        if (gameState == GameState.ENDED_SUCCESS_GOAT_REACHED_CABBAGE) {
            showMessageDialog(this, "Коза успешно достигла капусты!", MESSAGE_TITLE, PLAIN_MESSAGE);
            close();
        } else if (gameState == GameState.ENDED_FAILURE_STEPS_EXPIRED) {
            showMessageDialog(this, "У козы закончились шаги!", MESSAGE_TITLE, PLAIN_MESSAGE);
            close();
        }
    }

    @Override
    public void handleMessage(MessageSource source, MessageData data) {
        var gameMessage = (GameMessage) data;
        handleState(gameMessage.gameState);
    }

    private void close() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}