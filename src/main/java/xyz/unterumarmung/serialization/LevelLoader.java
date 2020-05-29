package xyz.unterumarmung.serialization;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.Level;
import xyz.unterumarmung.events.MessageSender;
import xyz.unterumarmung.events.SubscriptionHandler;
import xyz.unterumarmung.serialization.dto.LevelDto;
import xyz.unterumarmung.utils.collections.ReadOnlyList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LevelLoader {
    private final static String PACKAGE_NAME = LevelDto.class.getPackage().getName();
    private final @NotNull String directory;
    private final @NotNull DtoConverter dtoConverter;

    public LevelLoader(@NotNull String directory, @NotNull SubscriptionHandler subscriptionHandler, @NotNull MessageSender messageSender) {
        this.directory = directory;
        dtoConverter = new DtoConverter(subscriptionHandler, messageSender);
    }

    public ReadOnlyList<Level> levels() {
        Unmarshaller unmarshaller;
        try {
            var context = JAXBContext.newInstance(PACKAGE_NAME);
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            System.out.println(e.toString());
            return ReadOnlyList.empty();
        }

        var levels = new ArrayList<Level>(3);
        var files = files();
        for (var file : files) {
            try {
                Source source = new StreamSource(file);
                var level = unmarshaller.unmarshal(source, LevelDto.class).getValue();
                levels.add(dtoConverter.convert(level));
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return ReadOnlyList.fromList(levels);
    }

    private List<File> files() {
        final var files = new File(directory).listFiles();
        return Stream.of(files).filter(File::isFile).collect(Collectors.toList());
    }
}