package com.test.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.*;

@Entity // This tells Hibernate to make a table out of this class
@Table(name="`Persons`")
public class Person extends DateTimeEntity{

    private String name;
    private String address;
    private Integer age;
    private Date birthday;

    public Person(){
    }


    @OneToOne//(optional=false)
    @JoinColumn(name = "`cityId`")
    @JsonProperty("City")
    private City city;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<Value> Values;

    public List<Value> getValues() {
        return Values;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
