package com.test.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedSuperclass
public class DateTimeEntity extends AbstractEntity{

    protected DateTimeEntity(){
        createdAt = new Date();
        updatedAt = new Date();
    }

    @JsonIgnore
    @Column(name = "`createdAt`")
    private Date createdAt;

    @JsonIgnore
    @Column(name = "`updatedAt`")
    private Date updatedAt;

    @Transient
    DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public String getCreatedAt() {
        return df.format(createdAt);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return df.format(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
