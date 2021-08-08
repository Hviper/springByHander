package org.example.Controller;

import org.example.Annotation.AnnotationRealClassPath;
import org.example.Annotation.Autowired;
import org.example.Annotation.Component;
import org.example.Annotation.Scope;
import org.example.ConfigClass.Config;
import org.example.Interface.BeanPostProcessor;
import org.example.Interface.InitializingBean;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 拾光
 * @version 1.0
 */
public class ApplicationContentClassPath {
    Config config;

    List<String> classpathList = new ArrayList<>();

    List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    Map<String,BeanDefinition> beanDefinitionMap = new HashMap<>();
    public Map<String,Object> SingleObjectsMap = new HashMap<>();

    public Object getBean(String BeanName){
        if(SingleObjectsMap.containsKey(BeanName)){
            return SingleObjectsMap.get(BeanName);
        }
        if(beanDefinitionMap.containsKey(BeanName)){
            return CreateBeanObject(BeanName,beanDefinitionMap.get(BeanName));
        }
        return null;
    }

    private Object CreateBeanObject(String beanName, BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getClazz();

        Object instance = null;
        try {
            instance = clazz.getConstructor().newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //对Bean的属性尝试进行注入
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(field.isAnnotationPresent(Autowired.class)){
                field.setAccessible(true);
                //这里默认为根据字段的 "fieldName" 进行从容器中进行查找
                String name = field.getName();
                Object bean = getBean(name);
                try {
                    field.set(instance,bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        //实例化后
        //初始化前
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
        }

        //初始化。。。这里可以将instance实例进行代理
        if(instance instanceof InitializingBean){
            ((InitializingBean)instance).afterPropertiesSet();
        }

        //初始化后
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            beanPostProcessor.postProcessAfterInitialization(instance,beanName);
        }


        return instance;

    }


    public ApplicationContentClassPath(Config config) {
        this.config = config;
        scanner(config);

        CreateSingleBean();
    }

    private void CreateSingleBean() {
        //开始将所有单例的BeanDefinition先创建出来，存储在Map中，外界请求单例的直接返回即可
        beanDefinitionMap.keySet().forEach(BeanName->{
            BeanDefinition beanDefinition = beanDefinitionMap.get(BeanName);
            if("Single".equals(beanDefinition.getScope())){
                Class<?> clazz = beanDefinition.getClazz();
                Object instance = null;
                try {
                    instance = clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SingleObjectsMap.put(BeanName,instance);
            }
        });
    }

    private void scanner(Config config) {
        Class<? extends Config> aClass = config.getClass();
        if(aClass.isAnnotationPresent(AnnotationRealClassPath.class)){
            String classPath = aClass.getDeclaredAnnotation(AnnotationRealClassPath.class).value().replace('.', '/');;

            URL resource = ApplicationContentClassPath.class.getClassLoader().getResource(classPath);
            assert resource != null;
            String path = resource.getPath();
            System.out.println(path);
            File file = null;
            try {
                file = new File(URLDecoder.decode(path,"UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert file != null;
            getClassesFromPath(file);

            //扫描创建实例
            for (String clazz : classpathList) {
                try {
                    Class<?> cls = Class.forName(clazz);
                    if (cls.isAnnotationPresent(Component.class)) {
                        //对拓展类进行存储
                        if (BeanPostProcessor.class.isAssignableFrom(cls)) {
                            BeanPostProcessor instance = null;
                            try {
                                instance = (BeanPostProcessor) cls.getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            beanPostProcessorList.add(instance);
                            continue;
                        }


                        Component declaredAnnotation = cls.getDeclaredAnnotation(Component.class);

                        //获得组件名字BeanName
                        String BeanName = declaredAnnotation.value();

                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setClazz(cls);
                        if (cls.isAnnotationPresent(Scope.class)) {
                            Scope scopeName = cls.getDeclaredAnnotation(Scope.class);
                            String scope = scopeName.value();
                            beanDefinition.setScope(scope);
                            //没有Scope这个注解默认为单例模式
                        } else {
                            beanDefinition.setScope("Single");
                        }

                        beanDefinitionMap.put(BeanName, beanDefinition);

                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private void getClassesFromPath(File file) {
        if(file.isDirectory()){
            File[] files = file.listFiles();
            assert files != null;
            for (File file1 : files) {
                if(file1.isDirectory()){
                    getClassesFromPath(file1);
                }else{
                    String absolutePath = file1.getAbsolutePath();
                    String org = absolutePath.substring(absolutePath.indexOf("org"), absolutePath.indexOf(".class"));
                    classpathList.add(org.replace("\\","."));
                }
            }
        }
    }
}
