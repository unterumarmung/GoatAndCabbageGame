package xyz.unterumarmung.view;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.Level;
import xyz.unterumarmung.serialization.LevelLoader;
import xyz.unterumarmung.utils.collections.ReadOnlyList;
import xyz.unterumarmung.view.providers.ImageProvider;

import javax.swing.*;

import static java.lang.Integer.parseInt;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showInputDialog;

public class LevelChooser {
    private final @NotNull LevelLoader levelLoader;
    private final @NotNull ImageProvider iconProvider;

    public LevelChooser(@NotNull LevelLoader levelLoader, @NotNull ImageProvider iconProvider) {
        this.levelLoader = levelLoader;
        this.iconProvider = iconProvider;
    }

    public Level levelByUser() {
        final var levels = levelLoader.levels();
        final var levelStrings = levelsToStrings(levels);

        final var userReply = showInputDialog(
                null,
                "Выберете уровень для игры из списка ниже:",
                "Выбор уровня",
                PLAIN_MESSAGE,
                new ImageIcon(iconProvider.image()),
                levelStrings,
                null);

        if (userReply == null)
            return null;

        final var levelId = extractIdFromString((String) userReply);
        return findLevelById(levels, levelId);
    }

    private Object[] levelsToStrings(@NotNull ReadOnlyList<Level> levels) {
        return levels.stream().map(level -> level.id + ". " + level.description).toArray();
    }

    private int extractIdFromString(@NotNull String levelString) {
        final var dotIndex = levelString.indexOf('.');
        final var idString = levelString.substring(0, dotIndex);
        return parseInt(idString);
    }

    private Level findLevelById(@NotNull ReadOnlyList<Level> levels, int id) {
        for (final var level : levels) {
            if (level.id == id)
                return level;
        }
        return null;
    }
}
