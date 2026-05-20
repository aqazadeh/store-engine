package az.kon.academy.avro.plugin.classloader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class PluginClassLoaderFactory {

    private final ClassLoader parent;

    public PluginClassLoaderFactory(ClassLoader parent) {
        this.parent = parent;
    }

    public URLClassLoader create(List<String> classpathElements) throws Exception {
        URL[] urls = classpathElements.stream()
                .map(el -> {
                    try {
                        return new File(el).toURI().toURL();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(URL[]::new);
        return new URLClassLoader(urls, parent);
    }
}
