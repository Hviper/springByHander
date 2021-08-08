package org.example.Test.customizeClassloader;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import org.junit.Test;
/**
 * @author 拾光
 * @version 1.0
 */
public class TestMain {

    @Test
    public void test2() throws Exception {
        URL url = new URL("jar:file:/E:/mysql_jar包/mysql-connector-java-5.1.49.jar!/com/mysql/jdbc/Clob.class");
        InputStream inputStream = null;
        int len = -1;
        byte[] buffer = new byte[1024];
        byte[] res = null ;
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer();
        try{
            inputStream = url.openStream();
            while((len=inputStream.read(buffer))!=-1){
                byteArrayBuffer.write(buffer,0,len);
            }
            res = byteArrayBuffer.getRawData();

            inputStream.close();
            byteArrayBuffer.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println(Arrays.toString(res));
    }


    @Test
    public void test() throws Exception {
        ClassloaderByHander classloader = new ClassloaderByHander("E:\\mysql_jar包\\mysql-connector-java-5.1.49\\");
        Class<?> aClass = classloader.loadClass("com.mysql.jdbc.Extension");
        System.out.println(aClass);
    }
}
