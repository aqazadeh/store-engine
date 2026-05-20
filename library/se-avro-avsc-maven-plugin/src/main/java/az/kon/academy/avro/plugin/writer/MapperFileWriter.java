package az.kon.academy.avro.plugin.writer;

import az.kon.academy.avro.plugin.mapper.MapperSpec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MapperFileWriter {

    private final File outputDirectory;

    public MapperFileWriter(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public Path write(MapperSpec spec) throws IOException {
        Path packageDir = outputDirectory.toPath().resolve(spec.mapperPackage.replace('.', '/'));
        Files.createDirectories(packageDir);

        Path mapperFile = packageDir.resolve(spec.mapperName + ".java");
        Files.writeString(mapperFile, spec.source);
        return mapperFile;
    }
}
