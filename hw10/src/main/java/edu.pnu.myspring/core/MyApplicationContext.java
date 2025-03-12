package edu.pnu.myspring.core;

import edu.pnu.myspring.dispatcher.MyHandlerAdapter;
import edu.pnu.myspring.dispatcher.MyHandlerMapping;

import java.io.File;
import java.net.URL;
import java.util.*;
public class MyApplicationContext {



    private Map<Class<?>, Object> beanRegistry = new HashMap<>();

    private List<Object> beansToAutowire = new ArrayList<>();

    private Map<Class<?>, Object> controllerClasses = new HashMap<>();



    private final MyHandlerMapping handlerMapping;

    private final MyHandlerAdapter handlerAdapter;



    public MyApplicationContext(String basePackage) {

        scanAndRegisterBeans(basePackage);

        this.handlerMapping = new MyHandlerMapping(controllerClasses);

        this.handlerAdapter = new MyHandlerAdapter(handlerMapping);

        processAutowiring();

    }



    public MyHandlerAdapter getHandlerAdapter() {

        return handlerAdapter;

    }

    public MyHandlerMapping getHandlerMapping() {

        return handlerMapping;

    }



    public void scanAndRegisterBeans(String basePackage) {

        try {

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            String path = basePackage.replace('.', '/');



            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {

                URL resource = resources.nextElement();

                String protocol = resource.getProtocol();



                if (protocol.equals("file")) {

                    File directory = new File(resource.getFile());

                    for (File file : directory.listFiles()) {

                        processFile(file, basePackage);

                    }

                } else if (protocol.equals("jar")) {

                    JarURLConnection conn = (JarURLConnection) resource.openConnection();

                    JarFile jarFile = conn.getJarFile();



                    Enumeration<JarEntry> entries = jarFile.entries();

                    while (entries.hasMoreElements()) {

                        JarEntry entry = entries.nextElement();

                        String entryName = entry.getName();

                        if (entryName.endsWith(".class") && entryName.startsWith(path)) {

                            String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);

                            Class<?> clazz = Class.forName(className);

                            processClass(clazz);

                        }

                    }

                }

            }

        } catch (Exception e) {

            throw new RuntimeException("Failed to scan and register beans", e);

        }

    }



    private void processFile(File file, String basePackage) throws ClassNotFoundException {

        if (file.isDirectory()) {

            for (File subFile : file.listFiles()) {

                processFile(subFile, basePackage + "." + file.getName());

            }

        } else if (file.getName().endsWith(".class")) {

            String className = basePackage + '.' + file.getName().substring(0, file.getName().length() - 6);

            Class<?> clazz = Class.forName(className);

            processClass(clazz);

        }

    }



    private void processClass(Class<?> clazz) {

        if (hasAnnotationOrMetaAnnotation(clazz, MyComponent.class) /*... any other annotations*/) {

            System.out.println(" - Registering bean: " + clazz.getName());

            registerBean(clazz);

        }

    }


}
