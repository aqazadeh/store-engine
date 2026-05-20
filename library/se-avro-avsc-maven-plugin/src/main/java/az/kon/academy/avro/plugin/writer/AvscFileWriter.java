package az.kon.academy.avro.plugin.writer;

import org.apache.avro.Schema;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AvscFileWriter {

    private final File outputDirectory;
    private final boolean prettyPrint;
    private final boolean snakeCaseFileNames;

    public AvscFileWriter(File outputDirectory, boolean prettyPrint, boolean snakeCaseFileNames) {
        this.outputDirectory = outputDirectory;
        this.prettyPrint = prettyPrint;
        this.snakeCaseFileNames = snakeCaseFileNames;
    }

    @SuppressWarnings("deprecation")
    public Path write(Schema schema, String avroModelBasePackage) throws IOException {
        String namespace = schema.getNamespace();
        String suffix = namespace.substring(avroModelBasePackage.length()).replace('.', '/');
        if (suffix.startsWith("/")) suffix = suffix.substring(1);

        File dir = suffix.isEmpty() ? outputDirectory : new File(outputDirectory, suffix);
        Files.createDirectories(dir.toPath());

        String fileName = resolveFileName(schema.getName());
        Path avscFile = new File(dir, fileName).toPath();
        Files.writeString(avscFile, schema.toString(prettyPrint));
        return avscFile;
    }

    private String resolveFileName(String schemaName) {
        String baseName = snakeCaseFileNames ? toSnakeCase(schemaName) : schemaName;
        return baseName + ".avsc";
    }

    private String toSnakeCase(String name) {
        return name.replaceAll("([a-z\\d])([A-Z])", "$1_$2")
                   .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                   .toLowerCase();
    }
}
