package com.darkfoxdev.tesi.targetlint;

import java.util.*;
import java.util.jar.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @y.exclude
 */
public class ExtensionLoader<C> {

    public List<Class<? extends C>> loadJars(String directory, Class<C> parentClass) {
        List<Class<? extends C>> classes = new ArrayList<>();
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".jar"));
        for (File jarFile : files) {
            classes.addAll(loadClasses(jarFile, parentClass));
        }
        return classes;
    }

    public List<Class<? extends C>> loadClasses(File jarFile, Class<C> parentClass) {
        List<Class<? extends C>> classes = new ArrayList<>();
        try {
            URL[] urls = new URL[]{jarFile.toURI().toURL()};
            URLClassLoader classLoader = new URLClassLoader(urls,parentClass.getClassLoader());

            JarFile jar = new JarFile(jarFile.toString());
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = (JarEntry) entries.nextElement();

                if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                    continue;
                }

                String className = entry.getName().substring(0, entry.getName().length() - 6);
                className = className.replace('/', '.');
                try {
                    Class<?> loadedClass = classLoader.loadClass(className);
                    Class<? extends C> newClass = loadedClass.asSubclass(parentClass);
                    classes.add(newClass);
                } catch (ClassCastException e) {
                    continue;
                }

            }
            jar.close();
            classLoader.close();


        } catch (MalformedURLException e) {
            TLBridge.log(e.getStackTrace().toString());
        } catch (Exception e) {
            TLBridge.log(e.getStackTrace().toString());
        }
        return classes;

    }

    public List<Class<? extends C>> loadClasses(String directory, String jarName, Class<C> parentClass) {
        File jarFile = new File(directory + jarName);
        return loadClasses(jarFile, parentClass);
    }

}
