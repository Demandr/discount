package com.example.oleksandr.discount.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    private String number;

    private String password;

    private String name;

    private String surname;

    private String birthdate;

    private String sex;

    private String region;

    private String email;

    public User(@NonNull String number, String password) {
        this.number = number;
        this.password = password;
    }

    @Ignore
    public User(@NonNull String number, String password, String name, String surname, String birthdate, String sex, String region, String email) {
        this.number = number;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.sex = sex;
        this.region = region;
        this.email = email;
    }

    @NonNull
    public String getNumber() {
        return number;
    }

    public void setNumber(@NonNull String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
