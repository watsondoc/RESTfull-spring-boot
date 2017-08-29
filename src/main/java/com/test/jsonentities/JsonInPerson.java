package com.test.jsonentities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.entities.Attribute;
import com.test.entities.Person;
import com.test.entities.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class JsonInPerson {

    private Long id;
    private String name;
    private String address;
    private Integer age;
    private Date birthday;

    @JsonIgnore
    private Person updatedPerson;

    @JsonProperty("City")
    private String city;

    public JsonInPerson(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setUpdatedPerson(Person updatedPerson) {
        this.updatedPerson = updatedPerson;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getAge() {
        return age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Person getUpdatedPerson() {
        return updatedPerson;
    }

    public String getCity() {
        return city;
    }

    public Person getDefaultPerson(){
        Person updatedPerson = new Person();
        updatedPerson.setName(this.getName());
        updatedPerson.setAddress(this.getAddress());
        updatedPerson.setAge(this.getAge());
        updatedPerson.setBirthday(this.getBirthday());
        return updatedPerson;
    }

}
