package org.example.Controller;

import org.example.Annotation.Component;
import org.example.Interface.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 拾光
 * @version 1.0
 */
@Component("BeanPostProcessorImp")
public class BeanPostProcessorImp implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        //这里由bean的其他条件，比如bean中指定对应的注解的内容判断是否需要得到生成代理对象
        // 这里可以生成代理对象，用简单的TBean.equals(beanName)的方式进行判断是否需要进行创建代理对象
        //根据实例对象bean生成代理对象
        if("TBean".equals(beanName)){
            Object proxyInstance = Proxy.newProxyInstance(BeanPostProcessorImp.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    System.out.println("代理逻辑。。。。。。。。。。。。。日志打印前。。。。。。。。。。。。。。。。");
                    Object invoke = method.invoke(bean, args);
                    System.out.println("代理逻辑。。。。。。。。。。。。。日志打印后。。。。。。。。。。。。。。。。");
                    return invoke;
                }
            });
            System.out.println("代理结束");
            return proxyInstance;
        }

        return bean;
    }
}
