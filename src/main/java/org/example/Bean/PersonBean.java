package org.example.Bean;

import org.example.Annotation.Component;

/**
 * @author 拾光
 * @version 1.0
 */
@Component("PersonBean")
public class PersonBean {
    private String name;
    private int age;

    public PersonBean() {
    }

    public PersonBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "PersonBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
