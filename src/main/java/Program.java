import model.GameState;
import model.LevelBuilder;
import utils.Direction;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var game = new LevelBuilder().buildSimpleGame();
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
