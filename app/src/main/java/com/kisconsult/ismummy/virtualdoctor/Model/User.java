package com.kisconsult.ismummy.virtualdoctor.Model;

import java.io.Serializable;

/**
 * Created by ISMUMMY on 6/10/2016.
 */
public class User implements Serializable {

    private String fullname, username, mobile, image, department, bgroup, gender, category, password, email;
    private double height;
    private int age;


    public User() {

    }

    public void setDepartment(String dept) {
        department = dept;
    }

    public String getDepartment() {
        return department;
    }

    public void setBgroup(String bg) {
        bgroup = bg;
    }

    public String getBgroup() {
        return bgroup;
    }

    public void setGender(String sex) {
        gender = sex;
    }

    public String getGender() {
        return gender;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return this.image;
    }
}
