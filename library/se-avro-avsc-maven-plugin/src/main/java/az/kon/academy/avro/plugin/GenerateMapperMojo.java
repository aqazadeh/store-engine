package az.kon.academy.avro.plugin;

import az.kon.academy.avro.plugin.mapper.MapperSourceBuilder;
import az.kon.academy.avro.plugin.mapper.MapperSpec;
import az.kon.academy.avro.plugin.scanner.AvscFileCollector;
import az.kon.academy.avro.plugin.writer.MapperFileWriter;
import org.apache.avro.Schema;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Mojo(
        name = "generate-mapper",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES
)
public class GenerateMapperMojo extends AbstractMojo {

    /** Directory containing the generated .avsc files to read. */
    @Parameter(defaultValue = "${project.basedir}/src/main/avro", property = "avro.mapper.avroDirectory")
    private File avroDirectory;

    /** Directory where generated mapper interfaces will be written. */
    @Parameter(defaultValue = "${project.basedir}/src/main/java", property = "avro.mapper.outputDirectory")
    private File outputDirectory;

    /** MapStruct component model: default, spring, cdi, jakarta. */
    @Parameter(defaultValue = "default", property = "avro.mapper.componentModel")
    private String componentModel;

    /** Fully qualified class name of the MapStruct shared config (optional). */
    @Parameter(property = "avro.mapper.mapStructConfigClass")
    private String mapStructConfigClass;

    /** Suffix to strip from the Avro model name when deriving the mapper name. */
    @Parameter(defaultValue = "AvroModel", property = "avro.mapper.avroModelSuffix")
    private String avroModelSuffix;

    /** Skip mapper generation entirely. */
    @Parameter(defaultValue = "false", property = "avro.mapper.skip")
    private boolean skip;

    /** Fail the build if mapper generation encounters an error. */
    @Parameter(defaultValue = "true", property = "avro.mapper.failOnError")
    private boolean failOnError;

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("Skipping mapper generation (avro.mapper.skip=true)");
            return;
        }
        try {
            run();
        } catch (MojoExecutionException e) {
            throw e;
        } catch (Exception e) {
            if (failOnError) {
                throw new MojoExecutionException("Mapper generation failed", e);
            }
            getLog().warn("Mapper generation failed: " + e.getMessage(), e);
        }
    }

    private void run() throws Exception {
        if (!avroDirectory.isDirectory()) {
            getLog().warn("Avro directory not found, skipping mapper generation: " + avroDirectory);
            return;
        }

        AvscFileCollector collector = new AvscFileCollector();
        MapperSourceBuilder sourceBuilder = new MapperSourceBuilder(componentModel, mapStructConfigClass, avroModelSuffix);
        MapperFileWriter writer = new MapperFileWriter(outputDirectory);

        List<Path> avscFiles = collector.collect(avroDirectory);
        for (Path avscFile : avscFiles) {
            Schema schema = new Schema.Parser().parse(avscFile.toFile());
            MapperSpec spec = sourceBuilder.build(schema);
            getLog().info("  -> " + writer.write(spec));
        }
        getLog().info("Generated " + avscFiles.size() + " mapper interface(s) into " + outputDirectory);
    }
}
