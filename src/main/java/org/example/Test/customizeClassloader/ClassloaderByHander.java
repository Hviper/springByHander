package org.example.Test.customizeClassloader;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import java.io.FileInputStream;
import org.junit.Test;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureClassLoader;

/**
 * @author 拾光
 * @version 1.0
 * 自定义类加载器
 * 1.继承一个系统类加载器
 * 2.覆盖父类的findClass并调用defineClass方法
 */
public class ClassloaderByHander extends SecureClassLoader {


    String classpath;
    public ClassloaderByHander(String classpath){
        this.classpath=classpath;

    }

    public Class<?> findClass(String name) {
        byte[] b = loadClassData(name);
        assert b != null;
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassData(String name) {
        // load the class data from the connection
        //拼接文件路径
        String path = this.classpath + name.replace('.', '/').concat(".class");
        ByteArrayBuffer byteArray = new ByteArrayBuffer();
        try {
            int len = -1;
            byte[] buffer = new byte[2048];
            FileInputStream fileInputStream = new FileInputStream(path);
            while((len=fileInputStream.read(buffer))!=-1){
                byteArray.write(buffer,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArray.getRawData();
    }
}
