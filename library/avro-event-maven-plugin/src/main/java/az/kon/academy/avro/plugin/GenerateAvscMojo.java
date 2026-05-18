package az.kon.academy.avro.plugin;

import az.kon.academy.avro.plugin.schema.AvroSchemaBuilder;
import org.apache.avro.Conversion;
import org.apache.avro.Schema;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

@Mojo(
        name = "generate-avsc",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.COMPILE
)
public class GenerateAvscMojo extends AbstractMojo {

    @Parameter(required = true)
    private String eventBasePackage;

    @Parameter(defaultValue = "${project.basedir}/src/main/avro")
    private File outputDirectory;

    @Parameter(defaultValue = "${project.compileClasspathElements}", readonly = true)
    private List<String> compileClasspathElements;

    /** Fully-qualified class names of additional Avro Conversion implementations to register. */
    @Parameter
    private List<String> conversions = new ArrayList<>();

    @Override
    public void execute() throws MojoExecutionException {
        try (URLClassLoader classLoader = buildClassLoader()) {
            List<Conversion<?>> conversionInstances = loadConversions(classLoader);
            AvroSchemaBuilder schemaBuilder = new AvroSchemaBuilder(conversionInstances);

            String avroModelBasePackage = deriveAvroModelPackage(eventBasePackage);
            int count = 0;

            for (Class<?> eventClass : findEventClasses(classLoader)) {
                Optional<Schema> schema = schemaBuilder.buildSchema(eventClass, eventBasePackage);
                if (schema.isPresent()) {
                    writeAvsc(schema.get(), avroModelBasePackage);
                    count++;
                }
            }
            getLog().info("Generated " + count + " avsc file(s) into " + outputDirectory);
        } catch (Exception e) {
            throw new MojoExecutionException("Avsc generation failed", e);
        }
    }

    private URLClassLoader buildClassLoader() throws Exception {
        URL[] urls = new URL[compileClasspathElements.size()];
        for (int i = 0; i < compileClasspathElements.size(); i++) {
            urls[i] = new File(compileClasspathElements.get(i)).toURI().toURL();
        }
        return new URLClassLoader(urls, getClass().getClassLoader());
    }

    private List<Conversion<?>> loadConversions(URLClassLoader classLoader) throws Exception {
        List<Conversion<?>> result = new ArrayList<>();
        for (String className : conversions) {
            Class<?> cls = classLoader.loadClass(className);
            result.add((Conversion<?>) cls.getDeclaredConstructor().newInstance());
        }
        return result;
    }

    private List<Class<?>> findEventClasses(URLClassLoader classLoader) throws Exception {
        List<Class<?>> result = new ArrayList<>();
        String packagePath = eventBasePackage.replace('.', '/');

        for (URL url : classLoader.getURLs()) {
            String path = url.toURI().getPath();
            if (path.endsWith(".jar")) {
                collectFromJar(path, packagePath, classLoader, result);
            } else {
                collectFromDirectory(path, packagePath, classLoader, result);
            }
        }
        return result;
    }

    private void collectFromJar(String jarPath, String packagePath, URLClassLoader cl, List<Class<?>> result) {
        try (JarFile jar = new JarFile(jarPath)) {
            jar.stream()
                    .map(JarEntry::getName)
                    .filter(name -> name.startsWith(packagePath) && name.endsWith("Event.class"))
                    .forEach(name -> {
                        String className = name.replace('/', '.').replace(".class", "");
                        try {
                            result.add(cl.loadClass(className));
                        } catch (ClassNotFoundException e) {
                            getLog().warn("Could not load class: " + className);
                        }
                    });
        } catch (IOException e) {
            getLog().warn("Could not read jar: " + jarPath);
        }
    }

    private void collectFromDirectory(String dirPath, String packagePath, URLClassLoader cl, List<Class<?>> result) {
        Path root = new File(dirPath).toPath();
        Path packageDir = root.resolve(packagePath);
        if (!Files.isDirectory(packageDir)) return;

        try (Stream<Path> walk = Files.walk(packageDir)) {
            walk.filter(p -> p.toString().endsWith("Event.class"))
                    .forEach(p -> {
                        String className = root.relativize(p).toString()
                                .replace(File.separatorChar, '.')
                                .replace(".class", "");
                        try {
                            result.add(cl.loadClass(className));
                        } catch (ClassNotFoundException e) {
                            getLog().warn("Could not load class: " + className);
                        }
                    });
        } catch (IOException e) {
            getLog().warn("Could not walk directory: " + dirPath);
        }
    }

    private void writeAvsc(Schema schema, String avroModelBasePackage) throws IOException {
        String namespace = schema.getNamespace();
        String suffix = namespace.substring(avroModelBasePackage.length()).replace('.', '/');
        if (suffix.startsWith("/")) suffix = suffix.substring(1);

        File dir = suffix.isEmpty() ? outputDirectory : new File(outputDirectory, suffix);
        Files.createDirectories(dir.toPath());

        String fileName = schema.getName().replace("AvroModel", "Event") + ".avsc";
        Path avscFile = new File(dir, fileName).toPath();
        Files.writeString(avscFile, schema.toString(true));
        getLog().info("  -> " + avscFile);
    }

    private String deriveAvroModelPackage(String eventBasePackage) {
        int lastDot = eventBasePackage.lastIndexOf('.');
        return eventBasePackage.substring(0, lastDot) + ".avro.model";
    }
}
