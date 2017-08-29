package com.test.entities;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
@Table(name="`Cities`")
public class City extends DateTimeEntity{

    private String name;

    public City(){
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}