package org.example.Bean;

import org.example.Annotation.Component;
import org.example.Annotation.Scope;

/**
 * @author 拾光
 * @version 1.0
 */

@Component("StudentBean")
@Scope("Prototype")
public class StudentBean {
    private String name;
    private int age;
    public StudentBean() {
    }

    public StudentBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "StudentBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
