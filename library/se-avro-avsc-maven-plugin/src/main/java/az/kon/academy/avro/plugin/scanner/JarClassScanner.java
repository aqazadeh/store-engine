package az.kon.academy.avro.plugin.scanner;

import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassScanner {

    private final Log log;

    public JarClassScanner(Log log) {
        this.log = log;
    }

    public List<Class<?>> scan(URLClassLoader classLoader, String basePackage) throws Exception {
        List<Class<?>> result = new ArrayList<>();
        String packagePath = basePackage.replace('.', '/');

        for (URL url : classLoader.getURLs()) {
            String path = url.toURI().getPath();
            if (path.endsWith(".jar")) {
                collectFromJar(path, packagePath, classLoader, result);
            }
        }
        return result;
    }

    private void collectFromJar(String jarPath, String packagePath, URLClassLoader cl, List<Class<?>> result) {
        try (JarFile jar = new JarFile(jarPath)) {
            jar.stream()
                    .filter(e -> !e.isDirectory())
                    .map(JarEntry::getName)
                    .filter(name -> name.startsWith(packagePath + "/") && name.endsWith(".class"))
                    .filter(name -> !name.contains("$"))
                    .map(name -> name.replace('/', '.').replace(".class", ""))
                    .forEach(className -> {
                        try {
                            Class<?> clazz = cl.loadClass(className);
                            if (clazz.isInterface()) {
                                return;
                            }
                            result.add(clazz);
                        } catch (ClassNotFoundException e) {
                            log.warn("Could not load class: " + className + " - " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            log.warn("Could not read jar: " + jarPath + " - " + e.getMessage());
        }
    }
}
