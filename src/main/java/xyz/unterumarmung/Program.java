package xyz.unterumarmung;

import xyz.unterumarmung.events.MessageBridge;
import xyz.unterumarmung.model.GameState;
import xyz.unterumarmung.model.LevelBuilder;
import xyz.unterumarmung.serialization.LevelLoader;
import xyz.unterumarmung.utils.Direction;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var messageBridge = new MessageBridge();
        var levelLoader = new LevelLoader("levels", messageBridge, messageBridge);
        var levels = levelLoader.levels();
        System.out.println(levels.size());
        var game = levels.get(0).game();
        game.start();
        while (true) {
            System.out.print("Введите направление движения козы: ");
            var direction = directionFromString(scanner.next());
            if (direction == null)
                continue;
            game.gameField().goat().move(direction);
            System.out.println("Текущее состояние игры:" + game.gameState());
            if (game.gameState() != GameState.CONTINUING)
                break;
        }
    }

    private static Direction directionFromString(String string) {
        return switch (string) {
            case "С", "N" -> Direction.NORTH;
            case "Ю", "S" -> Direction.SOUTH;
            case "В", "E" -> Direction.EAST;
            case "З", "W" -> Direction.WEST;
            default -> null;
        };
    }
}
