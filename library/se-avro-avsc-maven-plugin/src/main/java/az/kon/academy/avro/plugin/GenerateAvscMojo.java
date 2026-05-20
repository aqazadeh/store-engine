package az.kon.academy.avro.plugin;

import az.kon.academy.avro.plugin.classloader.PluginClassLoaderFactory;
import az.kon.academy.avro.plugin.scanner.JarClassScanner;
import az.kon.academy.avro.plugin.schema.AvroSchemaBuilder;
import az.kon.academy.avro.plugin.writer.AvscFileWriter;
import org.apache.avro.Schema;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Optional;

@Mojo(
        name = "generate-avsc",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.COMPILE
)
public class GenerateAvscMojo extends AbstractMojo {

    /** Base package to scan for event classes (e.g. az.kon.academy.event). */
    @Parameter(required = true, property = "avro.inputPackage")
    private String inputPackage;

    /** Directory where generated .avsc files will be written. */
    @Parameter(defaultValue = "${project.basedir}/src/main/avro", property = "avro.outputDirectory")
    private File outputDirectory;

    @Parameter(defaultValue = "${project.compileClasspathElements}", readonly = true)
    private List<String> compileClasspathElements;

    /** Pretty-print generated .avsc files. */
    @Parameter(defaultValue = "false", property = "avro.avsc  .prettyPrint")
    private boolean prettyPrint;

    /** Class name suffix used to identify event classes (default: Event). */
    @Parameter(defaultValue = "Event", property = "avro.avsc.classSuffix")
    private String classSuffix;

    /** Skip .avsc generation entirely. */
    @Parameter(defaultValue = "false", property = "avro.avsc.skip")
    private boolean skip;

    /** Fail the build if schema generation encounters an error. */
    @Parameter(defaultValue = "true", property = "avro.avsc.failOnError")
    private boolean failOnError;

    /** Write .avsc file names in snake_case (e.g. order_placed_avro_model.avsc). */
    @Parameter(defaultValue = "true", property = "avro.avsc.snakeCaseFileNames")
    private boolean snakeCaseFileNames;

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("Skipping avsc generation (avro.skip=true)");
            return;
        }
        try {
            run();
        } catch (MojoExecutionException e) {
            throw e;
        } catch (Exception e) {
            if (failOnError) {
                throw new MojoExecutionException("Avsc generation failed", e);
            }
            getLog().warn("Avsc generation failed: " + e.getMessage(), e);
        }
    }

    private void run() throws Exception {
        PluginClassLoaderFactory classLoaderFactory = new PluginClassLoaderFactory(getClass().getClassLoader());
        JarClassScanner scanner = new JarClassScanner(getLog());
        AvroSchemaBuilder schemaBuilder = new AvroSchemaBuilder(classSuffix);
        AvscFileWriter writer = new AvscFileWriter(outputDirectory, prettyPrint, snakeCaseFileNames);

        String avroModelBasePackage = deriveAvroModelPackage(inputPackage);
        int count = 0;

        try (URLClassLoader classLoader = classLoaderFactory.create(compileClasspathElements)) {
            for (Class<?> clazz : scanner.scan(classLoader, inputPackage)) {
                Optional<Schema> schema = schemaBuilder.buildSchema(clazz, inputPackage);
                if (schema.isPresent()) {
                    getLog().info("  -> " + writer.write(schema.get(), avroModelBasePackage));
                    count++;
                }
            }
        }
        getLog().info("Generated " + count + " avsc file(s) into " + outputDirectory);
    }

    private String deriveAvroModelPackage(String eventBasePackage) {
        int lastDot = eventBasePackage.lastIndexOf('.');
        return eventBasePackage.substring(0, lastDot) + ".avro.model";
    }
}
