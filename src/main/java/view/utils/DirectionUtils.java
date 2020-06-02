package view.utils;

import utils.Direction;

import java.awt.event.KeyEvent;

public class DirectionUtils {
    public static Direction keyCodeToDirection(int keyCode) {
        return switch (keyCode) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> Direction.NORTH;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> Direction.SOUTH;
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> Direction.WEST;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> Direction.EAST;
            default -> throw new IllegalArgumentException("Key code: " + keyCode + " is not supported");
        };
    }

    public static boolean isDirectionKeyCode(int keyCode) {
        return switch (keyCode) {
            case KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D -> true;
            default -> false;
        };
    }
}
