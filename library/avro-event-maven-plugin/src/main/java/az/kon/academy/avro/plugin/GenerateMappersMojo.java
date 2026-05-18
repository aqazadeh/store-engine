package az.kon.academy.avro.plugin;

import org.apache.avro.Schema;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mojo(
        name = "generate-mappers",
        defaultPhase = LifecyclePhase.PROCESS_SOURCES
)
public class GenerateMappersMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.basedir}/src/main/avro")
    private File avroDirectory;

    @Parameter(defaultValue = "${project.basedir}/src/main/java")
    private File outputDirectory;

    @Parameter(required = true)
    private String mapStructConfigClass;

    @Override
    public void execute() throws MojoExecutionException {
        if (!avroDirectory.isDirectory()) {
            getLog().warn("Avro directory not found, skipping mapper generation: " + avroDirectory);
            return;
        }

        try {
            List<Path> avscFiles = collectAvscFiles();
            for (Path avscFile : avscFiles) {
                generateMapper(avscFile);
            }
            getLog().info("Generated " + avscFiles.size() + " mapper interface(s) into " + outputDirectory);
        } catch (Exception e) {
            throw new MojoExecutionException("Mapper generation failed", e);
        }
    }

    private List<Path> collectAvscFiles() throws IOException {
        try (Stream<Path> walk = Files.walk(avroDirectory.toPath())) {
            return walk
                    .filter(p -> p.toString().endsWith(".avsc"))
                    .collect(Collectors.toList());
        }
    }

    private void generateMapper(Path avscFile) throws IOException {
        Schema schema = new Schema.Parser().parse(avscFile.toFile());

        String modelName = schema.getName();           // e.g. ProductCreatedAvroModel
        String namespace = schema.getNamespace();      // e.g. az.kon.academy.catalog.avro.model.product

        String baseName = modelName.replace("AvroModel", "");              // ProductCreated
        String eventName = baseName + "Event";                             // ProductCreatedEvent
        String mapperName = baseName + "EventAvroMapper";                  // ProductCreatedEventAvroMapper
        String mapperPackage = namespace.replace(".avro.model.", ".avro.mapper.");
        String eventPackage = deriveEventPackage(namespace);

        String content = buildMapperSource(mapperPackage, namespace, modelName, eventPackage, eventName, mapperName);

        Path packageDir = outputDirectory.toPath().resolve(mapperPackage.replace('.', '/'));
        Files.createDirectories(packageDir);

        Path mapperFile = packageDir.resolve(mapperName + ".java");
        Files.writeString(mapperFile, content);
        getLog().info("  -> " + mapperFile);
    }

    private String deriveEventPackage(String avroModelNamespace) {
        // "az.kon.academy.catalog.avro.model.product" -> "az.kon.academy.catalog.event.product"
        int avroModelIdx = avroModelNamespace.indexOf(".avro.model");
        String catalogBase = avroModelNamespace.substring(0, avroModelIdx);
        String subPackageSuffix = avroModelNamespace.substring(avroModelIdx + ".avro.model".length());
        return catalogBase + ".event" + subPackageSuffix;
    }

    private String buildMapperSource(String mapperPackage, String modelNamespace, String modelName,
                                     String eventPackage, String eventName, String mapperName) {
        String configSimpleName = mapStructConfigClass.substring(mapStructConfigClass.lastIndexOf('.') + 1);
        return "package " + mapperPackage + ";\n\n"
                + "import " + mapStructConfigClass + ";\n"
                + "import " + modelNamespace + "." + modelName + ";\n"
                + "import " + eventPackage + "." + eventName + ";\n"
                + "import org.mapstruct.Mapper;\n"
                + "import org.mapstruct.factory.Mappers;\n\n"
                + "@Mapper(config = " + configSimpleName + ".class)\n"
                + "public interface " + mapperName + " {\n"
                + "    " + mapperName + " INSTANCE = Mappers.getMapper(" + mapperName + ".class);\n\n"
                + "    " + modelName + " toAvro(" + eventName + " event);\n\n"
                + "    " + eventName + " toEvent(" + modelName + " avroModel);\n"
                + "}\n";
    }
}
