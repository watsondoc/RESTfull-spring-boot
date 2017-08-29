package com.test.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
@Table(name="`Values`")
public class Value extends AbstractEntity{

    public Value(Attribute attr, Person person){
        this.attr=attr;
        this.person=person;
    }

    public Value(){

    }

    @ManyToOne
    @JoinColumn(name = "`personId`", nullable = false)
    private Person person;

    public Attribute getAttr() {
        return attr;
    }

    public void setAttr(Attribute attr) {
        this.attr = attr;
    }

    @ManyToOne
    @JoinColumn(name = "`attrId`", nullable = false)
    private Attribute attr;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Person getPerson(){
        return person;
    }

    public void setPerson(Person person){
        this.person = person;
    }

    private String value;

}
