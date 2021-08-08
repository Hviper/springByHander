package org.example.Test;
import org.example.Bean.StudentBean;
import org.example.ConfigClass.Config;
import org.example.Controller.ApplicationContentClassPath;
import org.example.Interface.BeanPostProcessor;
import org.testng.annotations.Test;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ServiceLoader;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author 拾光
 * @version 1.0
 */
public class TestMain {
    @Test
    public void test(){

        ApplicationContentClassPath content = new ApplicationContentClassPath(new Config());
        System.out.println(content.SingleObjectsMap);
        Object s1 = content.getBean("StudentBean");
        System.out.println(s1.hashCode());
        Object s2 = content.getBean("StudentBean");
        System.out.println(s2.hashCode());
    }
    @Test
    public void test2() throws Exception {
        //https://www.bilibili.com/video/BV16T4y1P79h?p=4
        URL url = new URL("file:E:\\mysql_jar包\\mysql-connector-java-5.1.49.jar");
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
        Class<?> aClass = urlClassLoader.loadClass("com.mysql.jdbc.Driver");

        System.out.println(aClass);
    }


    @Test
    public void test3() throws Exception {
        ServiceLoader<BeanPostProcessor> load = ServiceLoader.load(BeanPostProcessor.class);

    }
}
