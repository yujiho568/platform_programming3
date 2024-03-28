package edu.pnu.myspring.core;

import edu.pnu.myspring.annotations.*;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
//package edu.pnu.myspring.core
//기존 - service = new repository(생성)
//spring - 이미 생성되어있는 repository를 service에 집어넣음, 인스턴스를 하나만 가져가기 위해
//@Autowired private repository
//registry의 만들어놓은 repository를 집어넣음
//autowired - 의존성 주입
//key(class)(PersonController, PersonService,PersonRepository), registry(Map)-value(instance)(new~~)
public class MyApplicationContext {
    private Map<Class<?>, Object> beanRegistry = new HashMap<>();

    private List<Object> beansToAutowire = new ArrayList<>();

    private Map<Object, Method> postConstructMethodRegistry = new HashMap<>();

    private Map<Object, Method> preDestroyMethodRegistry = new HashMap<>();

    private Map<Class<?>, Object> controllerClasses = new HashMap<>();



    public MyApplicationContext(String basePackage) {

        scanAndRegisterBeans(basePackage);

        processAutowiring();

    }

    private void scanAndRegisterBeans(String basePackage){
        String path = basePackage.replace('.','/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if(resource==null) return;
        File directory = new File(resource.getFile());
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            System.out.println("Directory not found or is not a valid directory for package: " + basePackage);
            return;
        }

        File[] files = directory.listFiles();

        // Check if files are null
        if (files == null) {
            System.out.println("No files found in the directory for package: " + basePackage);
            return;
        }

        for (File file : files) {
            if(file.isDirectory()){
                scanAndRegisterBeans(basePackage+'.'+file.getName());
            }
            else if(file.getName().endsWith(".class")){
                String className = basePackage + '.' + file.getName().substring(0, file.getName().length()-6);
                try{
                    Class<?> clazz = Class.forName(className);
                    if(clazz.isAnnotationPresent(MyRestController.class)||
                            clazz.isAnnotationPresent(MyService.class)||
                            clazz.isAnnotationPresent(MyRepository.class)){
                        registerBean(clazz);
                    }
                }catch (ClassNotFoundException e){
                    throw new RuntimeException("Failed to scan class");
                }
            }
        }
    }
    private boolean hasAnnotationOrMetaAnnotation(Class<?> clazz, Class<MyComponent> targetAnnotation){
        if(clazz.isAnnotationPresent(targetAnnotation)){
            return true;
        }
        for(Annotation annotation : clazz.getDeclaredAnnotations()){
            if(annotation.annotationType().isAnnotationPresent(targetAnnotation)){
                return true;
            }
        }
        return false;
    }
    private void processAutowiring() {

        // implement your code
        for(Object bean : beansToAutowire){
            for(Field field : bean.getClass().getDeclaredFields()){
                if(field.isAnnotationPresent(MyAutowired.class)){
                    Object dependency = getBean(field.getType());
                    field.setAccessible(true);

                    try{
                        field.set(bean,dependency);
                    }catch (IllegalAccessException e){
                        throw new RuntimeException("Failed to inject dependency into field");
                    }
                }
            }
        }

    }

    public <T> void registerBean(Class<? extends T> beanClass) {

        // implement your code
        try{
            T beanInstance = beanClass.getDeclaredConstructor().newInstance();
            for(Method method : beanClass.getDeclaredMethods()){
                if(method.isAnnotationPresent(PostConstruct.class)){
                    postConstructMethodRegistry.put(beanInstance, method);
                }
                else if(method.isAnnotationPresent(PreDestroy.class)){
                    preDestroyMethodRegistry.put(beanInstance, method);
                }
            }

            beanRegistry.put(beanClass, beanInstance);
            if(beanClass.isAnnotationPresent(MyRestController.class)){
                controllerClasses.put(beanClass, beanInstance);
            }

            if(postConstructMethodRegistry.containsKey(beanInstance)){
                postConstructMethodRegistry.get(beanInstance).invoke(beanInstance);

            }
            beansToAutowire.add(beanInstance);
        }catch (Exception e){
            throw new RuntimeException("Failed to register bean");
        }

    }

    public <T> T getBean(Class<T> type) {
        return type.cast(beanRegistry.get(type));
        // implement your code

    }
    //빈이 삭제될때 호출되는 predestroy 메서드를 실행
    public void close() {
        for(Map.Entry<Object, Method> entry : preDestroyMethodRegistry.entrySet()){
            try{
                entry.getValue().invoke(entry.getKey());
            }catch (Exception e){
                throw new RuntimeException("Failed to execute @PreDestroy method for bean");
            }
        }
        beanRegistry.clear();

        // implement your code

    }

    public Map<Class<?>, Object> getControllerClasses() {
        return controllerClasses;
    }
}
