package com.kisconsult.ismummy.virtualdoctor.Model;

import java.io.Serializable;

/**
 * Created by ISMUMMY on 6/10/2016.
 */
public class Diagnosis implements Serializable {

    User user;
    String date, complain, time, drug, response;
    int id;

    public Diagnosis() {

    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setComplain(String complain) {
        this.complain = complain;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return this.response;
    }

    public String getComplain() {
        return complain;
    }

    public String getDrug() {
        return drug;
    }

    public int getId() {
        return id;
    }
}
