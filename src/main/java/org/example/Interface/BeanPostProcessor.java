package org.example.Interface;

/**
 * @author 拾光
 * @version 1.0
 * ctrl + alt + b :查看接口实现类
 */
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName);
    Object postProcessAfterInitialization(Object bean, String beanName);
}
