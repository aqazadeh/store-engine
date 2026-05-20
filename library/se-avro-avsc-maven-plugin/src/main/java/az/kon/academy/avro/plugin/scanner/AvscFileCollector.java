package az.kon.academy.avro.plugin.scanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AvscFileCollector {

    public List<Path> collect(File directory) throws IOException {
        try (Stream<Path> walk = Files.walk(directory.toPath())) {
            return walk
                    .filter(p -> p.toString().endsWith(".avsc"))
                    .collect(Collectors.toList());
        }
    }
}
