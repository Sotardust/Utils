package com.dai.utils.sqlite;

/**
 * Created by dai on 2017/9/14.
 */

public class UserInfo {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String name;
    private int age;
    private String sex;

    @Override
    public String toString() {
        return id + name + age + sex + "\n";
    }
}
